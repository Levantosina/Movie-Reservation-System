package com.movie.users.security;



import com.movie.jwt.jwt.JWTUtil;
import com.movie.users.users.OwnUsersDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;

/**
 * @author DMITRII LEVKIN on 10/10/2024
 * @project MovieReservationSystem
 */
@Component
public class UserJWTAuthenticationFilter extends OncePerRequestFilter {

    private  final JWTUtil jwtUtil;
    private final OwnUsersDetailsService ownUsersDetailsService;

    public UserJWTAuthenticationFilter(JWTUtil jwtUtil, OwnUsersDetailsService ownUsersDetailsService) {
        this.jwtUtil = jwtUtil;
        this.ownUsersDetailsService = ownUsersDetailsService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader= request.getHeader("Authorization");

        if(authHeader== null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt=authHeader.substring(7);
        String subject=jwtUtil.getSubject(jwt);

        if(subject != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= ownUsersDetailsService.loadUserByUsername(subject);

            if(jwtUtil.isTokenValid(jwt,userDetails.getUsername())){
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        filterChain.doFilter(request,response);
    }
}
