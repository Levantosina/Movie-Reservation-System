package com.movie.schedules.movieschedules;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Slf4j
@Repository("scheduleJdbc")
public class MovieMovieScheduleAccessService implements MovieScheduleDAO {

    private final JdbcTemplate jdbcTemplate;

    private final MovieScheduleRowMapper movieScheduleRowMapper;



    public MovieMovieScheduleAccessService(JdbcTemplate jdbcTemplate, MovieScheduleRowMapper movieScheduleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieScheduleRowMapper = movieScheduleRowMapper;

    }




    @Override
    public void createSchedule(MovieSchedule movieSchedule) {

       String sql = """
            INSERT INTO schedules (date, start_time, end_time,available_seats,cinema_id,movie_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,

                movieSchedule.getDate(),
                movieSchedule.getStartTime(),
                movieSchedule.getEndTime(),
                movieSchedule.getAvailableSeats(),
                movieSchedule.getCinemaId(),
                movieSchedule.getMovieId());

        log.info("Schedule created: " + movieSchedule);
    }

    public boolean scheduleExists(Long cinemaId, Long movieId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String sql = """
        SELECT COUNT(*) 
        FROM schedules 
        WHERE cinema_id = ? AND movie_id = ? AND date = ? AND start_time = ? AND end_time = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cinemaId, movieId, date, startTime, endTime);
        return count != null && count > 0;
    }
    @Override
    public List<MovieSchedule> selectAllSchedules() {
        String sql = """
            SELECT schedule_id,movie_id, cinema_id, date, start_time, end_time, available_seats FROM schedules
          """;
        return jdbcTemplate.query(sql,movieScheduleRowMapper);
    }

    @Transactional
    public void upDateSchedule(MovieSchedule updatedSchedule) {

        if (updatedSchedule.getDate()!= null) {
            var sql = """
            UPDATE schedules SET  date =? WHERE schedule_id =?
            """;
            jdbcTemplate.update(
                    sql,
                    updatedSchedule.getDate(),
                    updatedSchedule.getScheduleId());
        }

        if (updatedSchedule.getStartTime() != null) {
            var sql = """
                    UPDATE schedules SET  start_time = ? WHERE schedule_id = ?;
                    """;
            jdbcTemplate.update(sql,
                    updatedSchedule.getStartTime(),
                    updatedSchedule.getScheduleId());
        }

        if (updatedSchedule.getEndTime() != null) {
            var sql = """
                    UPDATE schedules SET  end_time = ? WHERE schedule_id = ?
                    """;

            jdbcTemplate.update(sql,
                    updatedSchedule.getEndTime(),
                    updatedSchedule.getScheduleId());
        }

        if (updatedSchedule.getAvailableSeats() != null) {
            var sql = """
            UPDATE schedules SET  available_seats = ? WHERE schedule_id = ?
            """;
            jdbcTemplate.update(sql,
                    updatedSchedule.getAvailableSeats(),
                    updatedSchedule.getScheduleId());
        }

        if (updatedSchedule.getCinemaId() != null) {
            var sql = """
                    UPDATE schedules SET  cinema_id = ? WHERE schedule_id = ?
                    """;
            jdbcTemplate.update(sql,
                    updatedSchedule.getCinemaId(),
                    updatedSchedule.getScheduleId());
        }

        if (updatedSchedule.getMovieId() != null) {
            var sql = """
            UPDATE schedules SET  movie_id = ? WHERE schedule_id = ?
            """;
            jdbcTemplate.update(sql,
                    updatedSchedule.getMovieId(),
                    updatedSchedule.getScheduleId());
        }
    }
    @Override
    public Optional<MovieSchedule> selectScheduleById(Long scheduleId) {
        String sql = "SELECT * FROM schedules WHERE schedule_id = ?";
        return jdbcTemplate.query(sql, movieScheduleRowMapper, scheduleId)
                .stream()
                .findFirst();
    }

    @Override
    public List<MovieSchedule> findByDate(LocalDate date) {
        String sql = "SELECT * FROM schedules WHERE date = ?";
        return jdbcTemplate.query(sql, movieScheduleRowMapper, date);
    }



    @Override
    public List<MovieSchedule> findByCinemaIdAndMovieId(Long cinemaId, Long movieId) {
        String sql = "SELECT * FROM schedules WHERE cinema_id = ? AND movie_id = ?";
        return jdbcTemplate.query(sql, movieScheduleRowMapper, cinemaId, movieId);
    }

    @Override
    public List<MovieSchedule> selectSchedulesByCinemaId(Long cinemaId) {
        String sql = " SELECT schedule_id, movie_id, cinema_id, date, start_time, end_time, available_seats \n" +
                "        FROM schedules WHERE cinema_id = ?";
        return jdbcTemplate.query(sql, movieScheduleRowMapper, cinemaId);
    }

    @Override
    public List<MovieSchedule> selectSchedulesByMovieId(Long movieId) {
        String sql = "SELECT * FROM schedules WHERE movie_id = ?";
        return jdbcTemplate.query(sql, movieScheduleRowMapper, movieId);
    }

    @Override
    public Optional<LocalTime> selectStartTimeByScheduleId(Long scheduleId) {
        String sql = """
            SELECT start_time FROM schedules WHERE schedule_id = ?
            """;
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getTime("start_time").toLocalTime(),
                scheduleId).stream().findFirst();
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";
        jdbcTemplate.update(sql, scheduleId);
    }

    @Transactional
    public void updateAvailableSeats(Long scheduleId, int availableSeats) {
        String sql = """
        UPDATE schedules 
        SET available_seats = ? 
        WHERE schedule_id = ?
    """;
        jdbcTemplate.update(sql, availableSeats, scheduleId);
        log.info("Updated available seats to {} for schedule ID {}", availableSeats, scheduleId);
    }


    @Override
    public long countSchedulesForMovieOnDate(long movieId, LocalDate scheduleDate) {
        String sql = """
       SELECT COUNT(*) FROM schedules WHERE movie_id = ? AND date = ?
       """;
        return jdbcTemplate.queryForObject(sql, Long.class, movieId, scheduleDate);
    }

    @Override
    public List<MovieSchedule> getSchedulesForCinemaOnDate(long cinemaId, LocalDate date) {
        String sql = """
       SELECT schedule_id, date, start_time, end_time, available_seats, cinema_id, movie_id
       FROM schedules
       WHERE cinema_id = ? AND date = ?
       """;
        return jdbcTemplate.query(sql, movieScheduleRowMapper, cinemaId, date);
    }

}