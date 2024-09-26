package com.movie.resrvation.cinema;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Component
public class CinemaRowMapper implements RowMapper<Cinema> {
    @Override
    public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Cinema.builder()
                .cinemaId(rs.getLong("cinema_id"))
                .cinemaName(rs.getString("cinema_name"))
                .cinemaLocation(rs.getString("cinema_location"))
                .build();
    }
}
