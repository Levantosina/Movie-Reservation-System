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
        @NotNull(message = "UserId id is required")
        Long userId,
        @NotNull(message = " MovieId id is required")
        Long movieId,
        @NotNull(message = "CinemaId id is required")
        Long cinemaId,
        @NotNull(message = "ScheduleId id is required")
        Long scheduleId,
        @NotNull(message = "Price is required")
        @Positive(message = "Available seats must be a positive number")
        BigDecimal price,
        @NotNull(message = "Date is required")
        Date date
) { }
