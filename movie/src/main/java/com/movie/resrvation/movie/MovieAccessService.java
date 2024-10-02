package com.movie.resrvation.movie;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */

@Repository("movieJdbc")
public class MovieAccessService implements  MovieDAO {
    private final JdbcTemplate jdbcTemplate;

    private  final MovieRowMapper movieRowMapper;

    public MovieAccessService(JdbcTemplate jdbcTemplate, MovieRowMapper movieRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieRowMapper = movieRowMapper;
    }

    @Override
    public List<Movie> selectAllMovies() {
        var sql = """
           SELECT  movie_name, year, country, genre, description
           FROM movies
           """;
        return jdbcTemplate.query(sql, movieRowMapper);
    }

    @Override
    public Optional<Movie> selectMovieById(Long movieId) {
        var sql = """
               SELECT  movie_name,year,country,genre,description
               FROM movies where movie_id=?
               """;
        return jdbcTemplate.query(sql,movieRowMapper,movieId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertMovie(Movie movie) {
        var sql= """
                INSERT INTO movies(movie_name,year,country,genre,description)
                VALUES (?,?,?,?,?)
                """;
        jdbcTemplate.update(sql,movie.getMovieName(),movie.getYear(),movie.getCountry(),movie.getGenre(),movie.getDescription());


    }

    @Override
    public void updateMovie(Movie updateMovie) {

    }
}
