package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.dto.response.TokenResponse;
import com.challenge.tenpo.repository.SessionRepository;
import com.challenge.tenpo.utils.JwtGenerator;
import com.challenge.tenpo.utils.JwtTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import static com.challenge.tenpo.utils.JwtTranslator.isValid;
import static com.challenge.tenpo.utils.Constant.START_TOKEN_INDEX;
import static com.challenge.tenpo.utils.Constant.TOKEN_BEARER;
import static com.challenge.tenpo.utils.Constant.TOKEN_HEADER;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
public class JwtService {

    private final SessionRepository sessionRepository;

    private final JwtGenerator jwtGenerator;

    private final String jwtSignKey;

    public JwtService(SessionRepository sessionRepository, JwtGenerator jwtGenerator, @Value("${jwt.sign.key}") String jwtSignKey) {
        this.sessionRepository = sessionRepository;
        this.jwtGenerator = jwtGenerator;
        this.jwtSignKey = jwtSignKey;
    }

    public static String getTokenFromHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        return removeBearerToToken(tokenHeader);
    }

    private static String removeBearerToToken(String token) {
        if (containBearer(token)) {
            return token.substring(START_TOKEN_INDEX);
        }
        return token;
    }

    private static boolean containBearer(String token) {
        return hasText(token) && token.startsWith(TOKEN_BEARER);
    }

    public TokenResponse generateTokenResponse() {
        String token = jwtGenerator.generate(AuthenticationService.getUsername(), jwtSignKey);
        return new TokenResponse(TOKEN_BEARER, token);
    }

    public boolean isValidToken(String token) {
        if (containBearer(token)) {
            token = removeBearerToToken(token);
        }
        return isValid(token, jwtSignKey);
    }

    public String getUsername(String token) {
        if (containBearer(token)) {
            token = removeBearerToToken(token);
        }
        return JwtTranslator.getUsername(token, jwtSignKey);
    }

}
