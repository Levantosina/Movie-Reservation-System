package com.movie.schedules.movieschedules;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 19/12/2024
 * @project Movie-Reservation-System
 */
public record ScheduleUpdateRequest (
        @NotNull(message = "Date cannot be null")
        LocalDate date,

        @JsonProperty("start_time")
        @NotNull(message = "Start time cannot be null")
        LocalTime startTime,

        @JsonProperty("end_time")
        @NotNull(message = "End time cannot be null")
        LocalTime endTime,

        @JsonProperty("available_seats")
        @NotNull(message = "Available seats cannot be null")
        @Positive(message = "Available seats must be a positive number")
        Integer availableSeats,

        @JsonProperty("cinema_id")
        @Positive(message = "Cinema ID must be a positive number")
        Long cinemaId,

        @JsonProperty("movie_id")
        @Positive(message = "Movie ID must be a positive number")
        Long movieId
){ }
