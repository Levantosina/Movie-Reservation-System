package com.movie.seats.seat;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Component
public class SeatRowMapper implements RowMapper<Seat> {
    @Override
    public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Seat.builder()

                .seatId(rs.getLong("seat_id"))
                .seatNumber(rs.getString("seat_number"))
                .row(rs.getString("row"))
                .type(rs.getString("type"))
                .cinemaId(rs.getLong("cinema_id"))
                .build();
    }
}
