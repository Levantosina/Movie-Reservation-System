package com.movie.seats.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author DMITRII LEVKIN on 17/12/2024
 * @project Movie-Reservation-System
 */
public record SeatUpdateRequest (
    Integer seatNumber,
    String row,
    String type,
    Long cinemaId,
    Boolean isOccupied
){}
