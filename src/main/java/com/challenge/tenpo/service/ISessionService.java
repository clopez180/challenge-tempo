package com.challenge.tenpo.service;

import com.challenge.tenpo.dto.request.LoginRequest;
import com.challenge.tenpo.dto.request.LogoutRequest;
import com.challenge.tenpo.dto.request.SignInRequest;
import com.challenge.tenpo.dto.response.TokenResponse;

public interface ISessionService {

    TokenResponse login(LoginRequest loginRequest);

    void logout(LogoutRequest logoutRequest);

    void signIn(SignInRequest signInRequest);

}
