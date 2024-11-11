package com.movie.jwt.jwt;


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
    private static final String SECRET_KEY = "INwJBaTWR1RhFkAeSihaFRp2jCT5CKYcsvQqXfxlP3E=";


    public String issueToken(String subject, String role) {
        return issueToken(subject, Map.of("role", role));
    }

    public String issueToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://github.com/Levantosina")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(15, DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }


    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }


    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }

    public String getRole(String token) {
        Object roleClaim = getClaims(token).get("role");
        if (roleClaim instanceof String) {
            return (String) roleClaim;
        } else if (roleClaim instanceof List<?>) {
            List<?> roles = (List<?>) roleClaim;
            if (!roles.isEmpty()) {
                return roles.get(0).toString();  // Return the first role (if there are multiple)
            }
        }
        return null;
    }
}


