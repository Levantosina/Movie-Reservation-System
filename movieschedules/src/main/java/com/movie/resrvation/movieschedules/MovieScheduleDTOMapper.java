package com.movie.resrvation.movieschedules;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Service
public class MovieScheduleDTOMapper implements Function<MovieSchedule, MovieScheduleDTO> {
    @Override
    public MovieScheduleDTO apply(MovieSchedule movieSchedule) {
        return new MovieScheduleDTO(
                movieSchedule.getScheduleId(),
                movieSchedule.getDate(),
                movieSchedule.getStartTime(),
                movieSchedule.getEndTime(),
                movieSchedule.getAvailableSeats(),
                movieSchedule.getCinemaId(),
                movieSchedule.getMovieId()
        );
    }
}
