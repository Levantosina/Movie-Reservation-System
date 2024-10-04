package com.movie.movie.movie;

import com.movie.movie.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
@Service
public class MovieService {

    private final MovieDAO movieDAO;
    private final MovieDTOMapper movieDTOMapper;

    public MovieService(@Qualifier("movieJdbc")MovieDAO movieDAO, MovieDTOMapper movieDTOMapper) {
        this.movieDAO = movieDAO;
        this.movieDTOMapper = movieDTOMapper;
    }

    public List<MovieDTO>getAllMovies(){
        return movieDAO.selectAllMovies()
                .stream()
                .map(movieDTOMapper)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovie(Long movieId){
        return  movieDAO.selectMovieById(movieId)
                .map(movieDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Movie with id [%s] not found".
                                        formatted(movieId)));

    }

    public void registerNewMovie(MovieRegistrationRequest movieRegistrationRequest){

        Movie movie= new Movie();
        movie.setMovieName(movieRegistrationRequest.movieName());
        movie.setYear(movieRegistrationRequest.year());
        movie.setCountry(movieRegistrationRequest.country());
        movie.setGenre(movieRegistrationRequest.genre());
        movie.setDescription(movieRegistrationRequest.description());

        movieDAO.insertMovie(movie);

    }
}
