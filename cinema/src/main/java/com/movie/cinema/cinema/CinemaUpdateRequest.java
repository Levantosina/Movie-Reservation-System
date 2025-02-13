package com.movie.cinema.cinema;

import jakarta.validation.constraints.NotBlank;

public record CinemaUpdateRequest(
    @NotBlank(message = "Cinema name is required")
    String cinemaName,
    @NotBlank(message = "Cinema Location is required")
    String cinemaLocation)
{
}