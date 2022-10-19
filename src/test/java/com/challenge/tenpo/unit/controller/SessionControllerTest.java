package com.challenge.tenpo.unit.controller;

import com.challenge.tenpo.controller.SessionController;
import com.challenge.tenpo.dto.request.LoginRequest;
import com.challenge.tenpo.dto.request.LogoutRequest;
import com.challenge.tenpo.dto.request.SignInRequest;
import com.challenge.tenpo.dto.response.ApiResponse;
import com.challenge.tenpo.dto.response.TokenResponse;
import com.challenge.tenpo.model.UserData;
import com.challenge.tenpo.repository.UserRepository;
import com.challenge.tenpo.service.ISessionService;
import com.challenge.tenpo.unit.BaseTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

public class SessionControllerTest extends BaseTest {

    @Mock
    ISessionService iSessionService;

    @InjectMocks
    SessionController sessionController;

    @Mock
    UserRepository userRepository;

    @Test
    public void givenUserRequest_whenSignup_thenOk() {

        SignInRequest sig = new SignInRequest();
        sig.setPassword("password");
        sig.setConfirmPassword("password");
        sig.setUsername("test");

        SignInRequest request = sig;
        UserData user = new UserData();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");

        when(userRepository.save(any(UserData.class))).thenReturn(user);

        ResponseEntity<ApiResponse> response = sessionController.singIn(request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), "The user was created.");
        assertEquals(response.getBody().getStatus(), OK.value());
    }

    @Test
    public void givenUserLoginRequest_whenLogin_thenOk() {
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("password");
        TokenResponse token = new TokenResponse();
        token.setKey("Bearer");
        token.setValue("fsgege32423fe23ffreftwtr343fesr4323525323");

        when(iSessionService.login(any(LoginRequest.class))).thenReturn(token);

        ResponseEntity<TokenResponse> response = sessionController.login(request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getValue(), token.getValue());
    }

    @Test
    public void givenUserLoginRequest_whenLogout_thenOk() {
        LogoutRequest logoutRequest = new LogoutRequest("token");

        doNothing().when(iSessionService).logout(logoutRequest);

        ResponseEntity<ApiResponse> response = sessionController.logout("token");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getMessage(), "Logout success.");
    }
}