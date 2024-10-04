package com.movie.schedules.movieschedules;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
public interface MovieScheduleDAO {



    void createSchedule(MovieSchedule movieSchedule);

    void upDateSchedule(MovieSchedule movieSchedule);

    Optional<MovieSchedule>selectScheduleById(Long scheduleId);

    List<MovieSchedule> findByDate(LocalDate date);
    //void insertSchedule(MovieSchedule movieSchedule);

    List<MovieSchedule> findByCinemaIdAndMovieId(Long cinemaId, Long movieId);

    List<MovieSchedule>selectSchedulesByCinemaId(Long cinemaId);
    List<MovieSchedule>selectSchedulesByMovieId(Long movieId);
    void deleteSchedule(Long scheduleId);


}
