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
    public void deleteCinemaById(Long CinemaId) {
        var sql = """
                DELETE from cinemas where cinema_id=?
                """;
       int res =  jdbcTemplate.update(sql,CinemaId);
        System.out.println("Delete cinema id: "+CinemaId);
    }

    @Override
    public void insertCinema(Cinema cinema) {
        var sql = """
    INSERT INTO cinemas (cinema_name, cinema_location) VALUES (?, ?)
    """;
    jdbcTemplate.update(sql, cinema.getCinemaName(), cinema.getCinemaLocation());
    }

    @Override
    public boolean existsById(Long cinemaId) {
        var sql = """
        SELECT COUNT(cinema_id) FROM cinemas WHERE cinema_id = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cinemaId);
        return count != null && count > 0;
    }

    @Override
    public void updateCinema(Cinema cinema) {

        if(cinema.getCinemaId() != null){
            var sql = """
                    UPDATE cinemas SET cinema_name=? where cinema_id=?
                    """;
            jdbcTemplate.update(sql, cinema.getCinemaName(), cinema.getCinemaId());
        }

        if(cinema.getCinemaLocation() != null){
            var sql = """
                    UPDATE cinemas SET cinema_location=? where cinema_id=?
                    """;
            jdbcTemplate.update(sql, cinema.getCinemaLocation(), cinema.getCinemaId());
        }
    }
}
