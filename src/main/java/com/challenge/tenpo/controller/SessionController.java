package com.challenge.tenpo.controller;

import com.challenge.tenpo.aspect.Track;
import com.challenge.tenpo.dto.request.LoginRequest;
import com.challenge.tenpo.dto.request.LogoutRequest;
import com.challenge.tenpo.dto.request.SignInRequest;
import com.challenge.tenpo.dto.response.ApiResponse;
import com.challenge.tenpo.dto.response.TokenResponse;
import com.challenge.tenpo.service.ISessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api("Session for user")
@RequestMapping(value = "/auth")
public class SessionController {

    @Autowired
    private final ISessionService sessionService;

    @Track(endpoint = "/auth/signup")
    @ApiOperation(value = "Sign in users", notes = "Register a user")
    @ApiResponses(
            value = {
                    @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully register"),
                    @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request - User already exists")
            })
    @PostMapping("/sign_in")
    public ResponseEntity<ApiResponse> singIn(@RequestBody @Valid SignInRequest signInRequest) {
        sessionService.signIn(signInRequest);
        return ResponseEntity.ok(new ApiResponse(OK.value(), "The user was created."));
    }

    @Track(endpoint = "/auth/login")
    @ApiOperation(value = "Login users", notes = "Login a user with username and password")
    @ApiResponses(
            value = {
                    @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully login with token"),
                    @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized")
            })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse token = sessionService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @Track(endpoint = "/auth/logout")
    @ApiOperation(value = "Logout users", notes = "Logout a user with given token")
    @ApiResponses(
            value = {
                    @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully logout - token expire"),
            })
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        sessionService.logout(new LogoutRequest(token));
        return ResponseEntity.ok(new ApiResponse(OK.value(), "Logout success."));
    }
}
