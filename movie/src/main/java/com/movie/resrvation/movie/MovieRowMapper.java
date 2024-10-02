package com.movie.resrvation.movie;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
@Component
public class MovieRowMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Movie.builder()

                .movieName(rs.getString("movie_name"))
                .year(rs.getInt("year"))
                .country(rs.getString("country"))
                .genre(rs.getString("genre"))
                .description(rs.getString("description"))
                .build();
    }
}
