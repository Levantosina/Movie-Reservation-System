package com.movie.common;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
public record ScheduleDTO(
        Long scheduleId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer availableSeats,
        Long cinemaId,
        Long movieId) { }