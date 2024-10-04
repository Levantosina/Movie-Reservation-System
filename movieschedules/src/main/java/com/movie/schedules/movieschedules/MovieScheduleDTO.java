package com.movie.schedules.movieschedules;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
public record MovieScheduleDTO(
        Long scheduleId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer availableSeats,
        Long cinemaId,
        Long movieId) {

}
