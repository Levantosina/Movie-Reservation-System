package com.movie.jwt.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Service("jwtUtilService")
public class JWTUtil {
    private static final String SECRET_KEY = "INwJBaTWR1RhFkAeSihaFRp2jCT5CKYcsvQqQfxl4TY=";

    public String issueToken(String username, String role) {
        return issueToken(username, Map.of("role", role));
    }

    public String issueToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
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
        String extractedUsername = getSubject(jwt);
        return extractedUsername.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }

    public String getRole(String token) {
        String role = getClaims(token).get("role", String.class);  // Corrected to "role"
        if (role != null && !role.trim().isEmpty()) {
            return role;
        }
        return null;
    }
}


