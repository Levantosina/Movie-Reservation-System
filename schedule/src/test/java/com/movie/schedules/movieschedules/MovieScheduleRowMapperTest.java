package com.movie.schedules.movieschedules;


import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author DMITRII LEVKIN on 18/12/2024
 * @project Movie-Reservation-System
 */
class MovieScheduleRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        long scheduleId = 34;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;

        MovieScheduleRowMapper movieScheduleRowMapper = new MovieScheduleRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("schedule_id")).thenReturn(1L);
        when(resultSet.getDate("date")).thenReturn(Date.valueOf(date));
        when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(startTime));
        when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(endTime));
        when(resultSet.getInt("available_seats")).thenReturn(availableSeats);
        when(resultSet.getLong("cinema_id")).thenReturn(cinemaId);
        when(resultSet.getLong("movie_id")).thenReturn(movieId);

        MovieSchedule actual =movieScheduleRowMapper.mapRow(resultSet,1);
        MovieSchedule expected = new MovieSchedule(1L,date,startTime,endTime,availableSeats,cinemaId,movieId);
        assertThat(actual).isEqualTo(expected);

    }
}

