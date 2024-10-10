package com.movie.users.jwt;

import com.movie.users.users.UsersDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private  final  JWTUtil jwtUtil;
    private final UsersDetailsService usersDetailsService;

    public JWTAuthenticationFilter(JWTUtil jwtUtil,UsersDetailsService usersDetailsService) {
        this.jwtUtil = jwtUtil;
        this.usersDetailsService = usersDetailsService;

    }

    @Override
    protected void doFilterInternal(@reactor.util.annotation.NonNull HttpServletRequest request,
                                    @reactor.util.annotation.NonNull HttpServletResponse response,
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
            UserDetails userDetails= usersDetailsService.loadUserByUsername(subject);

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
