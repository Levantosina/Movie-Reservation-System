package com.movie.resrvation.theater;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Component
public class TheaterRowMapper implements RowMapper<Theater> {
    @Override
    public Theater mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Theater.builder()
                .theaterId(rs.getLong("theater_id"))
                .theaterName(rs.getString("theater_name"))
                .theaterLocation(rs.getString("theater_location"))
                .build();
    }
}
