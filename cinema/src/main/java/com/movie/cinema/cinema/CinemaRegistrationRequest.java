package com.movie.cinema.cinema;

import jakarta.validation.constraints.NotBlank;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
public record CinemaRegistrationRequest(
        @NotBlank(message = "Cinema name is required")
        String cinemaName,
        @NotBlank(message = "Cinema Location is required")
        String cinemaLocation) {


}
