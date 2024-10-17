package com.movie.common;

import com.movie.common.UserDTO;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
public record AuthenticationResponse(String token, UserDTO userDTO) {}
