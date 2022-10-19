package com.challenge.tenpo.service.impl;

import com.challenge.tenpo.model.UserCredential;
import com.challenge.tenpo.model.UserData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static com.challenge.tenpo.utils.Constant.ANONYMOUS;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public static String getUsername() {
        return getUserDetails().map(userDetails -> userDetails.getUsername()).orElse(ANONYMOUS);
    }

    public static Long getUserId() {
        return getUserDetails().map(userDetails -> userDetails.getId()).orElse(null);
    }

    public static Optional<UserCredential> getUserDetails() {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserCredential) {
                return of((UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }
        } catch (Exception e) {
            log.error("Security context error.");
        }
        log.warn("User details could not be obtained. By default it will be anonymous.");
        return empty();
    }

    public static void generateCredential(HttpServletRequest httpServletRequest, UserData userData) {
        UserCredential userCredential = UserCredential.build(userData);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void generateCredential(UserData userData) {
        UserCredential userCredential = UserCredential.build(userData);
        setCredentialToContext(userCredential);
    }

    public static void generateInvalidCredential(String username) {
        UserCredential userCredential = UserCredential.build(username);
        userCredential.setCredentialsNonExpired(false);
        setCredentialToContext(userCredential);
    }

    private static void setCredentialToContext(UserCredential userCredential) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
