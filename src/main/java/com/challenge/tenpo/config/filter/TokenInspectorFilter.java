package com.challenge.tenpo.config.filter;

import com.challenge.tenpo.service.ISessionSecurityService;
import com.challenge.tenpo.service.impl.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.challenge.tenpo.service.impl.AuthenticationService.generateCredential;
import static com.challenge.tenpo.service.impl.JwtService.getTokenFromHeader;
import static java.util.Optional.ofNullable;

@Slf4j
@AllArgsConstructor
public class TokenInspectorFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    private JwtService jwtService;

    private ISessionSecurityService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ofNullable(getTokenFromHeader(httpServletRequest)).ifPresent(token ->
                verifyAndAuthorizeAccess(httpServletRequest, token));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void verifyAndAuthorizeAccess(HttpServletRequest httpServletRequest, String token) {
        if (jwtService.isValidToken(token) && sessionService.existSessionByToken(token)) {
            generateCredential(httpServletRequest, sessionService.getUserData(jwtService.getUsername(token)));
        } else {
            log.error("The token has already expired. Username: {}.", jwtService.getUsername(token));
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
