package com.movie.schedules.movieschedules;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
public interface MovieScheduleDAO {

    List<MovieSchedule>selectAllSchedules();

    void createSchedule(MovieSchedule movieSchedule);

    void upDateSchedule(MovieSchedule movieSchedule);

    Optional<MovieSchedule>selectScheduleById(Long scheduleId);


    List<MovieSchedule> findByDate(LocalDate date);

    List<MovieSchedule> findByCinemaIdAndMovieId(Long cinemaId, Long movieId);

    List<MovieSchedule>selectSchedulesByCinemaId(Long cinemaId);
    List<MovieSchedule>selectSchedulesByMovieId(Long movieId);
    void deleteSchedule(Long scheduleId);

    void updateAvailableSeats(Long scheduleId, int availableSeats);
    boolean scheduleExists(Long cinemaId, Long movieId, LocalDate date, LocalTime startTime, LocalTime endTime);


}
