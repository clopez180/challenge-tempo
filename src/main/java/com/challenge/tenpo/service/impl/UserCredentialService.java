package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.exception.InvalidUsernameException;
import com.challenge.tenpo.model.UserCredential;
import com.challenge.tenpo.model.UserData;
import com.challenge.tenpo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.challenge.tenpo.service.impl.AuthenticationService.generateInvalidCredential;
import static java.lang.String.format;

@Slf4j
@Service
@AllArgsConstructor
public class UserCredentialService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userRepository.findByUsername(username);
        if (user != null) {
            return UserCredential.build(user);
        }
        generateInvalidCredential(username);
        String msg = format("Username: %s is not registered", username);
        throw new InvalidUsernameException(msg);
    }
}
