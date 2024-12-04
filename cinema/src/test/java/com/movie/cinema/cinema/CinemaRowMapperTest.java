package com.movie.cinema.cinema;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DMITRII LEVKIN on 04/12/2024
 * @project Movie-Reservation-System
 */
class CinemaRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        CinemaRowMapper cinemaRowMapper = new CinemaRowMapper();

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("cinema_id")).thenReturn(5L);
        when(resultSet.getString("cinema_name")).thenReturn("Amazonia");
        when(resultSet.getString("cinema_location")).thenReturn("NY");

        Cinema actual = cinemaRowMapper.mapRow(resultSet,1);
        Cinema expected = new Cinema(5L,"Amazonia","NY");
        assertThat(actual).isEqualTo(expected);

    }
}
