package com.movie.ticket.ticket;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
public record TicketRegistrationRequest (

        @NotNull(message = " MovieId id is required")
        Long movieId,
        @NotNull(message = "CinemaId id is required")
        Long cinemaId,
        @NotNull(message = "Seat id is required")
        Long seatId,
        @NotNull(message = "ScheduleId id is required")
        Long scheduleId
) { }
