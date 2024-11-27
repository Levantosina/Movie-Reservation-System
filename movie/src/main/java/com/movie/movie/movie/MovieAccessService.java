package com.movie.movie.movie;

import jakarta.transaction.Transactional;
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
           SELECT  movie_id, movie_name, year, country, genre, description
           FROM movies
           """;
        return jdbcTemplate.query(sql, movieRowMapper);
    }

    @Override
    public Optional<Movie> selectMovieById(Long movieId) {
        var sql = """
               SELECT  movie_id,movie_name,year,country,genre,description
               FROM movies where movie_id=?
               """;
        return jdbcTemplate.query(sql,movieRowMapper,movieId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Movie> selectMovieByName(String name) {
        var sql = """
                SELECT * FROM movies where movie_name = ?
        
                """;
       return jdbcTemplate.query(sql,movieRowMapper,name)
                .stream()
                .findFirst();
    }

    @Override
    public void insertMovie(Movie movie) {
        var sql= """
                INSERT INTO movies(movie_name,year,country,genre,description)
                VALUES (?,?,?,?,?) RETURNING movie_id
                """;
      Long movieId =  jdbcTemplate.queryForObject(sql,
                Long.class,
                movie.getMovieName(),
                movie.getYear(),
                movie.getCountry(),
                movie.getGenre(),
                movie.getDescription());
        movie.setMovieId(movieId);


    }

    @Transactional
    public void updateMovie(Movie updateMovie) {

        if(updateMovie.getMovieName()!=null){
            var sql = """
                    UPDATE movies SET movie_name=? where movie_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateMovie.getMovieName(),
                    updateMovie.getMovieId()
            );

        }

        if(updateMovie.getYear()!=null){
            var sql = """
                    UPDATE movies SET year=? where movie_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateMovie.getYear(),
                    updateMovie.getMovieId()
            );

        }

        if(updateMovie.getCountry()!=null){
            var sql = """
                    UPDATE movies SET country=? where movie_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateMovie.getCountry(),
                    updateMovie.getMovieId()
            );

        }

        if(updateMovie.getGenre()!=null){
            var sql = """
                    UPDATE movies SET genre=? where movie_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateMovie.getGenre(),
                    updateMovie.getMovieId()
            );

        }

        if(updateMovie.getDescription()!=null){
            var sql = """
                    UPDATE movies SET description=? where movie_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateMovie.getDescription(),
                    updateMovie.getMovieId()
            );
        }
    }

    @Override
    public boolean existMovieWithId(Long movieId) {
        var sql= """
                SELECT count(movie_id)FROM movies
               where movie_id=?
               """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,movieId);
        return count!=null && count>0;
    }
}
