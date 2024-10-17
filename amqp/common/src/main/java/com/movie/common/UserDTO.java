package com.movie.common;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,

        String roleName


) {
}