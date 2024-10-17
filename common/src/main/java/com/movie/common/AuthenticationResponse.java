package com.movie.common;



/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
public record AuthenticationResponse(String token, UserDTO userDTO) {}
