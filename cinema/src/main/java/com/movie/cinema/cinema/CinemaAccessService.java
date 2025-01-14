package com.movie.cinema.cinema;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Repository("cinemaJdbc")
public class CinemaAccessService implements CinemaDAO{

    private final JdbcTemplate jdbcTemplate;

    private final CinemaRowMapper cinemaRowMapper ;

    public CinemaAccessService(JdbcTemplate jdbcTemplate, CinemaRowMapper cinemaRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.cinemaRowMapper = cinemaRowMapper;
    }

    @Override
    public List<Cinema> selectAllCinemas() {

        var sql = """
               SELECT cinema_id, cinema_name,cinema_location
               FROM cinemas
               """;
        return jdbcTemplate.query(sql,cinemaRowMapper);
    }

    @Override
    public Optional<Cinema> selectCinemaById(Long cinemaId) {
        var sql = """
               SELECT cinema_id, cinema_name,cinema_location
               FROM cinemas where cinema_id=?
               """;

        return jdbcTemplate.query(sql,cinemaRowMapper,cinemaId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Cinema> selectCinemaByCinemaName(String cinemaName) {
        var sql = """
               SELECT cinema_id, cinema_name,cinema_location
               FROM cinemas where cinema_name=?
               """;


        return jdbcTemplate.query(sql,cinemaRowMapper,cinemaName)
                .stream()
                .findFirst();
    }


    @Override
    public void insertCinema(Cinema cinema) {
        var sql = """
    INSERT INTO cinemas (cinema_name, cinema_location) VALUES (?, ?)
    """;
    jdbcTemplate.update(sql, cinema.getCinemaName(), cinema.getCinemaLocation());
    }

    @Override
    public boolean existsById(long cinemaId) {
        var sql = """
        SELECT COUNT(cinema_id) FROM cinemas WHERE cinema_id = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cinemaId);
        return count != null && count > 0;
    }


//    @Override
//    public boolean existCinemaWithName(String name) {
//        return false;
//    }

//    @Override
//    public void updateCinema(Cinema updateCinema) {
//
//    }


}
