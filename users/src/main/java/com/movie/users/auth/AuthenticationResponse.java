package com.movie.users.auth;

import com.movie.users.users.UserDTO;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
public record AuthenticationResponse( String token,UserDTO userDTO) {}
