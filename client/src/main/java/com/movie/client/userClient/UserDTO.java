package com.movie.client.userClient;

import jakarta.validation.constraints.NotBlank;

/**
 * @author DMITRII LEVKIN on 20/11/2024
 * @project Movie-Reservation-System
 */
public record UserDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        @NotBlank String role_name




) {
}