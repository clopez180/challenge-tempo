package com.challenge.tenpo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import java.security.Key;
import java.time.Instant;

import static io.jsonwebtoken.io.Decoders.BASE64;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Date.from;

@AllArgsConstructor
public class JwtGenerator {

    private int jwtTtl;

    private static Key generateKey(String jwtSignKey) {
        return Keys.hmacShaKeyFor(BASE64.decode(jwtSignKey));
    }

    public String generate(String username, String jwtSignKey) {
        Instant today = Instant.now();
        Instant expirationDate = today.plus(jwtTtl, MINUTES);
        return Jwts.builder().setSubject(username)
                .setIssuedAt(from(today))
                .setExpiration(from(expirationDate))
                .signWith(generateKey(jwtSignKey))
                .compact();
    }

}
