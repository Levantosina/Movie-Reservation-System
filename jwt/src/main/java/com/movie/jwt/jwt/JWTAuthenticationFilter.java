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
import java.util.Map;

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
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if the Authorization header is missing or does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or invalid on request to [{}]", request.getRequestURI());
            filterChain.doFilter(request, response); // Continue without setting authentication
            return;
        }

        // Extract the JWT from the Authorization header
        String jwt = authHeader.substring(7);
        String subject;
        try {
            // Validate the JWT and extract claims
            subject = jwtUtil.getSubject(jwt);
            Map<String, Object> claims = jwtUtil.getClaims(jwt);

            log.debug("Decoded JWT claims for user [{}]: {}", subject, claims);
        } catch (Exception e) {
            log.error("Invalid or expired JWT token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Respond with 401 Unauthorized
            return;
        }

        // Ensure the JWT contains a valid subject and that authentication is not already set
        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Extract the role from the JWT claims
                String role = jwtUtil.getClaims(jwt).get("role", String.class);
                log.info("Authenticated user: [{}], Role: [{}]", subject, role);
                // Validate the role
                if (role == null || role.trim().isEmpty()) {
                    log.error("Role is missing or invalid in token for user: [{}]", subject);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Respond with 403 Forbidden
                    return;
                }

                log.info("Role extracted from token for user [{}]: {}", subject, role);

                // Create authorities based on the role
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                // Create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(subject, jwt, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                log.info("Authenticated user [{}] successfully with role [{}]", subject, role);
            } catch (Exception e) {
                log.error("Failed authentication for user [{}]: {}", subject, e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Respond with 401 Unauthorized
                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}