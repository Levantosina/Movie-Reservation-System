package com.movie.seats.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
public record SeatRegistrationRequest(
        @NotNull(message = "Seat number name is required")
        Integer seatNumber,
        @NotBlank(message = "Row is required")
        String row,
        @NotBlank(message = "Type of seats name is required")
        String type,
        @NotNull(message = "Cinema id is required")
        Long cinemaId,
        @NotNull(message = "Seat occupation id is required")
        Boolean isOccupied
){}

