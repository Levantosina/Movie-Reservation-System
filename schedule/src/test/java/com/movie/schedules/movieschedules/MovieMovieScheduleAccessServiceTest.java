package com.movie.schedules.movieschedules;

import com.movie.schedules.ScheduleAbstractDaoUnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author DMITRII LEVKIN on 18/12/2024
 * @project Movie-Reservation-System
 */
@Slf4j
class MovieMovieScheduleAccessServiceTest extends ScheduleAbstractDaoUnitTest {


 private MovieMovieScheduleAccessService underTest;

    private final MovieScheduleRowMapper movieScheduleRowMapper = new MovieScheduleRowMapper();

   private final long scheduleId = 34;
    private final LocalDate date = LocalDate.parse("2024-12-03");
    private final LocalTime startTime = LocalTime.parse("10:00:00");
    private final LocalTime endTime = LocalTime.parse("12:00:00");
    private final  int availableSeats = 2;
    private final long cinemaId = 1;
    private final  long movieId = 1;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM schedules");
        underTest = new MovieMovieScheduleAccessService(getJdbcTemplate(),
                movieScheduleRowMapper);


    }
    @Test
    void selectAllSchedules() {

        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);
        List<MovieSchedule> actual = underTest.selectAllSchedules();
        assertThat(actual).isNotEmpty();



    }
    @Test
    void createSchedule() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);

        long scheduleId = underTest.selectAllSchedules()
                .stream()
                .map(MovieSchedule::getScheduleId)
                .findFirst()
                .orElseThrow();

        Optional<MovieSchedule> actual = underTest.selectScheduleById(scheduleId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }

    @Test
    void upDateSchedule() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);

        long scheduleId = underTest.selectAllSchedules()
                .stream()
                .map(MovieSchedule::getScheduleId)
                .findFirst()
                .orElseThrow();

        schedule.setScheduleId(scheduleId);

         LocalDate UpdatedDate = LocalDate.parse("2024-12-05");
         LocalTime UpdatedStartTime = LocalTime.parse("12:00:00");
         LocalTime UpdatedEndTime = LocalTime.parse("14:00:00");
         int UpdatedAvailableSeats = 10;
         long UpdatedCinemaId = 2;
         long UpdatedMovieId = 2;

         schedule.setDate(UpdatedDate);
         schedule.setStartTime(UpdatedStartTime);
         schedule.setEndTime(UpdatedEndTime);
         schedule.setAvailableSeats(UpdatedAvailableSeats);
         schedule.setCinemaId(UpdatedCinemaId);
         schedule.setMovieId(UpdatedMovieId);

         underTest.upDateSchedule(schedule);


        Optional<MovieSchedule> updatedActual = underTest.selectScheduleById(scheduleId);
        log.info("UPDATED ACTUAL:{}",updatedActual);
        log.info("UPDATED:{}",schedule);

        assertThat(updatedActual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getDate()).isEqualTo(UpdatedDate);
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
            log.info("UPDATED:{}",updatedActual);
            log.info("UPDATED:{}",schedule);
        });
    }

    @Test
    void selectScheduleById() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);

        long scheduleId = underTest.selectAllSchedules()
                .stream()
                .map(MovieSchedule::getScheduleId)
                .findFirst()
                .orElseThrow();

        Optional<MovieSchedule> actual = underTest.selectScheduleById(scheduleId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }


    @Test
    void findByDate() {

        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);


        List<MovieSchedule> actual = underTest.findByDate(date);
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);

        assertThat(actual.getFirst()).satisfies(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }


    @Test
    void findByCinemaIdAndMovieId() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);


        List<MovieSchedule> actual = underTest.findByCinemaIdAndMovieId(cinemaId,movieId);
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);

        assertThat(actual.getFirst()).satisfies(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }

    @Test
    void selectSchedulesByCinemaId() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);


        List<MovieSchedule> actual = underTest.selectSchedulesByCinemaId(cinemaId);
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);

        assertThat(actual.getFirst()).satisfies(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }

    @Test
    void selectSchedulesByMovieId() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);


        List<MovieSchedule> actual = underTest.selectSchedulesByMovieId(movieId);
        assertThat(actual).isNotEmpty();
        assertThat(actual).hasSize(1);

        assertThat(actual.getFirst()).satisfies(c -> {
            assertThat(c.getDate()).isEqualTo(schedule.getDate());
            assertThat(c.getStartTime()).isEqualTo(schedule.getStartTime());
            assertThat(c.getEndTime()).isEqualTo(schedule.getEndTime());
            assertThat(c.getAvailableSeats()).isEqualTo(schedule.getAvailableSeats());
            assertThat(c.getCinemaId()).isEqualTo(schedule.getCinemaId());
            assertThat(c.getMovieId()).isEqualTo(schedule.getMovieId());
        });
    }
    @Test
    void deleteSchedule() {
        MovieSchedule schedule = MovieSchedule.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .availableSeats(availableSeats)
                .cinemaId(cinemaId)
                .movieId(movieId)
                .build();
        underTest.createSchedule(schedule);

        long scheduleId = underTest.selectAllSchedules()
                .stream()
                .map(MovieSchedule::getScheduleId)
                .findFirst()
                .orElseThrow();

        underTest.deleteSchedule(scheduleId);
        Optional<MovieSchedule> deletedSchedule = underTest.selectScheduleById(scheduleId);
        assertThat(deletedSchedule).isNotPresent();

    }
}