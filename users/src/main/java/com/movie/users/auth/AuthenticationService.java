package com.movie.users.auth;

import com.movie.common.AuthenticationRequest;
import com.movie.common.AuthenticationResponse;
import com.movie.common.UserDTO;
import com.movie.jwt.jwt.JWTUtil;
import com.movie.users.users.User;
import com.movie.users.users.UserDTOMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserDTOMapper userDTOMapper;

    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UserDTOMapper userDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDTOMapper = userDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication =   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.username(),
                authenticationRequest.password()
        ));
        User principal=(User) authentication.getPrincipal();
        UserDTO userDTO=userDTOMapper.apply(principal);
        String token= jwtUtil.issueToken(userDTO.email(),userDTO.roleName());
        return  new AuthenticationResponse(token,userDTO);
    }

    public boolean validateToken(String token) {
        try {
            // Extract email (or username) from token
            String email = jwtUtil.getSubject(token);

            // Check if the token is valid
            return jwtUtil.isTokenValid(token, email);
        } catch (Exception e) {
            // Log the exception and return false if token is invalid
            return false;
        }
    }
}