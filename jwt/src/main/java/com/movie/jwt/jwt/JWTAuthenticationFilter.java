package com.movie.jwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DMITRII LEVKIN on 22/10/2024
 * @project MovieReservationSystem
 */
@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    public JWTAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or invalid.");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String subject = jwtUtil.getSubject(jwt);

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String role = jwtUtil.getClaims(jwt).get("role", String.class);
                log.info("Role extracted from token: {}", role);
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (role != null && !role.trim().isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority(role));
                } else {
                    log.warn("Role is null or empty: {}", role);
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                subject,
                                null,
                                authorities);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
            } catch (Exception e) {
                log.error("Authentication failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}