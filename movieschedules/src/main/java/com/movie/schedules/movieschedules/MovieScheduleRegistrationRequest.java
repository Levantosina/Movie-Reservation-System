package com.movie.schedules.movieschedules;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
public record MovieScheduleRegistrationRequest(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Long cinemaId,
        Long movieId

) {
}
