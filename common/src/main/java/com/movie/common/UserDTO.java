package com.movie.common;


import jakarta.validation.constraints.NotBlank;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        @NotBlank String role_name




) {
}