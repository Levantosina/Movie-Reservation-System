package com.movie.schedules.movieschedules;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
public record MovieScheduleRegistrationRequest(

        @NotNull(message = "Date cannot be null")
        LocalDate date,

        @NotNull(message = "Start time cannot be null")
        @JsonProperty("start_time")
        LocalTime startTime,

        @NotNull(message = "End time cannot be null")
        @JsonProperty("end_time")
        LocalTime endTime,

        @JsonProperty("cinema_id")
        @NotNull(message = "Cinema ID cannot be null")
        Long cinemaId,

        @JsonProperty("movie_id")
        @NotNull(message = "Movie ID cannot be null")
        Long movieId


) {
}
