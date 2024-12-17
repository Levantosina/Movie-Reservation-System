package com.movie.seats.seat;


import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
class SeatRowMapperTest {



    @Test
    void mapRow() throws SQLException  {
        SeatRowMapper seatRowMapper = new SeatRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("seat_id")).thenReturn(2L);
        when(resultSet.getInt("seat_number")).thenReturn(5);
        when(resultSet.getString("row")).thenReturn("A");
        when(resultSet.getString("type")).thenReturn("VIP");
        when(resultSet.getLong("cinema_id")).thenReturn(1L);
        when(resultSet.getBoolean("is_occupied")).thenReturn(true);

        Seat actual = seatRowMapper.mapRow(resultSet,1);
        Seat expected = new Seat(2L,5,"A","VIP",1L,true);
        assertThat(actual).isEqualTo(expected);
    }
}
