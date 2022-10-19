package com.challenge.tenpo.unit.service;

import com.challenge.tenpo.dto.request.LoginRequest;
import com.challenge.tenpo.dto.request.LogoutRequest;
import com.challenge.tenpo.dto.response.TokenResponse;
import com.challenge.tenpo.model.UserData;
import com.challenge.tenpo.repository.SessionRepository;
import com.challenge.tenpo.repository.UserRepository;
import com.challenge.tenpo.service.impl.AuthenticationService;
import com.challenge.tenpo.service.impl.JwtService;
import com.challenge.tenpo.service.impl.SessionService;
import com.challenge.tenpo.unit.BaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.challenge.tenpo.utils.Constant.TOKEN_BEARER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SessionServiceTest extends BaseTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    AuthenticationService authenticationResolver;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    SessionService sessionService;

    @Test
    public void givenUser_whenLogin_thenOk() {
        UserData user = new UserData();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        TokenResponse tokenmock = new TokenResponse(TOKEN_BEARER, "token");
        Authentication authentication = AuthenticationMocks.getDefault();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(jwtService.generateTokenResponse())
                .thenReturn(new TokenResponse(TOKEN_BEARER, "token"));
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("password");
        TokenResponse token = sessionService.login(request);

        assertNotNull(token);
        assertEquals(token.getKey(), tokenmock.getKey());
        assertEquals(token.getValue(), tokenmock.getValue());
    }
    @Test
    void givenUser_whenLogout_thenOk() {
        LogoutRequest logoutRequest = new LogoutRequest("token");
        when(jwtService.getUsername(anyString())).thenReturn("test");

        assertDoesNotThrow(() -> sessionService.logout(logoutRequest));
    }

}
