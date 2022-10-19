package com.challenge.tenpo.config;

import com.challenge.tenpo.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.challenge.tenpo.service.impl.JwtService.getTokenFromHeader;
import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR_MSJ_WITHOUT_TOKEN = "An access token is required. Login is required to obtain the token.";

    private static final String ERROR_MSJ_TOKEN_INVALID = "the token is invalid or expired.";

    private static final String APPLICATION_JSON = "application/json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        StringBuilder builder = new StringBuilder();
        builder.append(HttpStatus.UNAUTHORIZED.getReasonPhrase() + ". ");
        ofNullable(getTokenFromHeader(httpServletRequest)).ifPresentOrElse(token ->
                        builder.append(ERROR_MSJ_TOKEN_INVALID)
                , () -> builder.append(ERROR_MSJ_WITHOUT_TOKEN));

        ApiResponse apiResponseError = new ApiResponse(SC_UNAUTHORIZED, builder.toString());
        ResponseEntity responseEntity = new ResponseEntity<>(apiResponseError, UNAUTHORIZED);
        httpServletResponse.setContentType(APPLICATION_JSON);
        httpServletResponse.setStatus(SC_UNAUTHORIZED);
        httpServletResponse.getOutputStream().println(MAPPER.writeValueAsString(responseEntity));

    }
}
