package com.movie.users.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * @author DMITRII LEVKIN on 09/10/2024
 * @project MovieReservationSystem
 */

@Service
public class JWTUtil {
    private static final String SECRET_KEY = "foobar_123456789_foobar_123456789_foobar_123456789";

    public String issueToken(String subject) {

        return issueToken(subject, Map.of());
    }


    public String issueToken(String subject, String... scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, Map<String, Object> claims) {

        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://github.com/Levantosina")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(
                        Date.from(Instant.now().plus(15, DAYS)
                        )
                ).signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }


    private Key getSingingKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}

