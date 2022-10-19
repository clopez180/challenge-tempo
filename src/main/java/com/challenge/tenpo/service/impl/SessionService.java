package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.config.PasswordEncoder;
import com.challenge.tenpo.dto.request.LoginRequest;
import com.challenge.tenpo.dto.request.LogoutRequest;
import com.challenge.tenpo.dto.request.SignInRequest;
import com.challenge.tenpo.dto.response.TokenResponse;
import com.challenge.tenpo.exception.UsernameNotAvailableException;
import com.challenge.tenpo.model.SessionData;
import com.challenge.tenpo.model.UserData;
import com.challenge.tenpo.repository.SessionRepository;
import com.challenge.tenpo.repository.UserRepository;
import com.challenge.tenpo.service.ISessionSecurityService;
import com.challenge.tenpo.service.ISessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.challenge.tenpo.service.impl.AuthenticationService.generateCredential;
import static com.challenge.tenpo.service.impl.AuthenticationService.getUserDetails;
import static com.challenge.tenpo.service.impl.AuthenticationService.getUserId;
import static java.lang.String.format;

@Slf4j
@Service
@AllArgsConstructor
public class SessionService implements ISessionService, ISessionSecurityService {


    private  UserRepository userRepository;

    private  SessionRepository sessionRepository;

    private PasswordEncoder passwordEncoder;

    private  JwtService jwtService;

    private  AuthenticationService authenticationResolver;


    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationResolver.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        TokenResponse tokenResponse = jwtService.generateTokenResponse();
        if (!sessionRepository.existsByUsername(loginRequest.getUsername())) {
            sessionRepository.save(new SessionData(getUserId(), loginRequest.getUsername(), tokenResponse.getValue()));
            log.info("The {}'s session was created. Login was successful.", loginRequest.getUsername());
        } else {
            log.info("Username {} is already logged in. Returns the new token.", loginRequest.getUsername());
            updateSessionInformation(loginRequest, tokenResponse);
        }
        return tokenResponse;
    }

    @Override
    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        String username = jwtService.getUsername(logoutRequest.getToken());
        sessionRepository.deleteByUsername(username);
        getUserDetails().ifPresent(userDetails -> userDetails.setCredentialsNonExpired(false));
        log.info("Username {} logged out successfully", username);
    }

    @Override
    @Transactional
    public void signIn(SignInRequest signInRequest) {
        if (!userRepository.existsUserByUsername(signInRequest.getUsername())) {
            saveAndCreateSession(signInRequest);
            log.info("Username {} was created.", signInRequest.getUsername());
        } else {
            String msg = format("Username %s already exists.", signInRequest.getUsername());
            log.info(msg);
            throw new UsernameNotAvailableException(msg);
        }
    }

    @Override
    public boolean existSessionByToken(String token) {
        return sessionRepository.existsByToken(token);
    }

    @Override
    public UserData getUserData(String username) {
        return userRepository.findByUsername(username);
    }

    private void updateSessionInformation(LoginRequest loginRequest, TokenResponse tokenResponse) {
        SessionData currentSessionData = sessionRepository.getByUsername(loginRequest.getUsername());
        currentSessionData.setToken(tokenResponse.getValue());
        sessionRepository.save(currentSessionData);
    }

    private void saveAndCreateSession(SignInRequest signInRequest) {
        UserData newUser = new UserData(signInRequest.getUsername(), passwordEncoder.bCryptPasswordEncoder().encode(signInRequest.getPassword()));
        UserData userData = userRepository.save(newUser);
        generateCredential(userData);
    }

}
