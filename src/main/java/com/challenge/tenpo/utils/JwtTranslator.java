package com.challenge.tenpo.utils;

import com.challenge.tenpo.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import static java.util.Optional.ofNullable;

@Slf4j
public class JwtTranslator {

    public static String getUsername(String token, String signKey) {
        return ofNullable(getClaims(token, signKey)).map(claim -> claim.getSubject())
                .orElseThrow(() -> {
                    String msg = "User token is invalid.";
                    log.error(msg);
                    return new InvalidTokenException(msg);
                });
    }

    public static boolean isValid(String token, String signKey) {
        return getClaims(token, signKey) != null;
    }

    private static Claims getClaims(String token, String signKey) {
        if (token != null) {
            try {
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            } catch (SignatureException se) {
                log.error("Invalid Signature {}", se.getMessage());
            } catch (MalformedJwtException mje) {
                log.error("Invalid JWT token: {}", mje.getMessage());
            } catch (ExpiredJwtException eje) {
                log.error("Jwt token is expired: {}", eje.getMessage());
            } catch (UnsupportedJwtException uje) {
                log.error("Jwt token is unsupported: {}", uje.getMessage());
            } catch (IllegalArgumentException iae) {
                log.error("Jwt claims string is empty: {}", iae.getMessage());
            }
        }
        return null;
    }

}