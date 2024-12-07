package com.movie.movie.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
public record MovieRegistrationRequest(
        @NotBlank(message = "Movie name is required")
        String movieName,

        @NotNull(message = "Year is required")
        Integer year,

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Genre is required")
        String genre,

        @NotBlank(message = "Description is required")
        String description
) {}
