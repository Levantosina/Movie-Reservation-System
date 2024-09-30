package com.movie.resrvation.movie;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
@Service
public class MovieDTOMapper implements Function<Movie,MovieDTO> {
    @Override
    public MovieDTO apply(Movie movie) {
        return new MovieDTO(
                movie.getMovieId(),
                movie.getMovieName(),
                movie.getYear(),
                movie.getCountry(),
                movie.getGenre(),
                movie.getDescription()

        );
    }
}
