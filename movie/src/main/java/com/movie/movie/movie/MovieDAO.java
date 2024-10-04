package com.movie.movie.movie;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
public interface MovieDAO {

    List<Movie> selectAllMovies();

    Optional<Movie> selectMovieById(Long movieId);

    void insertMovie(Movie movie);

    void updateMovie(Movie updateMovie);
}
