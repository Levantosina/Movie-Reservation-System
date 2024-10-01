package com.movie.resrvation.movieschedules;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Component
public class MovieScheduleRowMapper implements RowMapper<MovieSchedule> {
    @Override
    public MovieSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MovieSchedule.builder()
                .scheduleId(rs.getLong("schedule_id"))
                .date(rs.getDate("date").toLocalDate())
                .startTime(rs.getTime("start_time").toLocalTime())
                .endTime(rs.getTime("end_time").toLocalTime())
                .availableSeats(rs.getInt("available_seats"))
                .cinemaId(rs.getLong("cinema_id"))
                .movieId(rs.getLong("movie_id"))
                .build();
    }
}
