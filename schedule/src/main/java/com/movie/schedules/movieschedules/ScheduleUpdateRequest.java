package com.movie.schedules.movieschedules;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 19/12/2024
 * @project Movie-Reservation-System
 */
public record ScheduleUpdateRequest (
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    Integer availableSeats,
    Long cinemaId,
    Long movieId
){ }
