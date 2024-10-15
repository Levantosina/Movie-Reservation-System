package com.movie.users.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author DMITRII LEVKIN on 15/10/2024
 * @project MovieReservationSystem
 */
public record PasswordResetRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String userName,
        @NotBlank(message = "Password is required")
        String newPassword
) {}


