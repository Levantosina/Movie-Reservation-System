package com.movie.resrvation.theater;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Repository("theaterJdbc")
public class TheaterAccessService implements TheaterDAO{

    private final JdbcTemplate jdbcTemplate;

    private final TheaterRowMapper theaterRowMapper;

    public TheaterAccessService(JdbcTemplate jdbcTemplate, TheaterRowMapper theaterRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.theaterRowMapper = theaterRowMapper;
    }

    @Override
    public List<Theater> selectAllTheaters() {

        var sql = """
               SELECT theater_id, theater_name,theater_location
               FROM theaters
               """;
        return jdbcTemplate.query(sql,theaterRowMapper);
    }

    @Override
    public Optional<Theater> selectTheaterById(Long theaterId) {
        var sql = """
               SELECT theater_id, theater_name,theater_location
               FROM theaters where theater_id=?
               """;

        return jdbcTemplate.query(sql,theaterRowMapper,theaterId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertTheater(Theater theater) {
        var sql = """
    INSERT INTO theaters (theater_name, theater_location) 
    VALUES (?, ?)
    """;
    jdbcTemplate.update(sql, theater.getTheaterName(), theater.getTheaterLocation());
    }



    @Override
    public boolean existTheaterWithName(String name) {
        return false;
    }

    @Override
    public void updateTheater(Theater updateTheater) {

    }


}
