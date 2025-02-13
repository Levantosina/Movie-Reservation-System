package com.movie.movie.movie;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;


import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
@Service
@Slf4j
public class MovieService {

    private final MovieDAO movieDAO;
    private final MovieDTOMapper movieDTOMapper;
    private final RabbitMqMessageProducer rabbitMqMessageProducer;


    public MovieService(@Qualifier("movieJdbc")MovieDAO movieDAO, MovieDTOMapper movieDTOMapper, RabbitMqMessageProducer rabbitMqMessageProducer) {
        this.movieDAO = movieDAO;
        this.movieDTOMapper = movieDTOMapper;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;

    }

    public List<MovieDTO>getAllMovies(){
        return movieDAO.selectAllMovies()
                .stream()
                .map(movieDTOMapper)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Long movieId){
        return  movieDAO.selectMovieById(movieId)
                .map(movieDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Movie with id [%s] not found".
                                        formatted(movieId)));

    }

    public String getMovieNameById(Long movieId) {
        return movieDAO.selectMovieById(movieId)
                .map(Movie::getMovieName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie with id [%s] not found".formatted(movieId)
                ));
    }

    public void registerNewMovie(MovieRegistrationRequest movieRegistrationRequest){

        String movieName = movieRegistrationRequest.movieName();

        if (movieDAO.selectMovieByName(movieName).isPresent()) {
            throw new DuplicateResourceException("Movie name must be unique.");
        }

        Movie movie= new Movie();
        movie.setMovieName(movieRegistrationRequest.movieName());
        movie.setYear(movieRegistrationRequest.year());
        movie.setCountry(movieRegistrationRequest.country());
        movie.setGenre(movieRegistrationRequest.genre());
        movie.setDescription(movieRegistrationRequest.description());

        movieDAO.insertMovie(movie);

        NotificationRequest notificationRequest = new NotificationRequest(
                movie.getMovieId(), "Movie created", "The movie " + movie.getMovieName() + " has been created."
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }
    public void updateMovie(Long movieId,MovieUpdateRequest movieUpdateRequest) {
        Movie movie = movieDAO.selectMovieById(movieId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movie with [%s] not found"
                                .formatted(movieId))); /// isolate exception!!!!!!

        movie.setMovieId(movieId);
        boolean changes = false;

        if (movieUpdateRequest.movieName() != null) {
            if (!movieUpdateRequest.movieName().equals(movie.getMovieName()) && movieDAO.selectMovieByName(movieUpdateRequest.movieName()).isPresent()) {
                throw new DuplicateResourceException("Movie name must be unique.");
            }
            movie.setMovieName(movieUpdateRequest.movieName());
            changes = true;
        }


        if (movieUpdateRequest.country() != null && !movieUpdateRequest.country().equals(movie.getCountry())) {
            movie.setCountry(movieUpdateRequest.country());
            changes = true;
        }

        if (movieUpdateRequest.year() != null && !movieUpdateRequest.year().equals(movie.getYear())) {
            movie.setYear(movieUpdateRequest.year());
            changes = true;
        }

        if (movieUpdateRequest.genre() != null && !movieUpdateRequest.genre().equals(movie.getGenre())) {
            movie.setGenre(movieUpdateRequest.genre());
            changes = true;
        }
        if (movieUpdateRequest.description() != null && !movieUpdateRequest.description().equals(movie.getDescription())){
            movie.setDescription(movieUpdateRequest.description());
        changes = true;
        }

        if(!changes){
            throw  new RequestValidationException("No changes detected");
        }
        log.info("Updating movie: {}", movie);

        movieDAO.updateMovie(movie);


        NotificationRequest notificationRequest = new NotificationRequest(
                movie.getMovieId(),
                "Movie was updated Successfully",
                String.format(" Movie  with id '%s' and name '%s' has been successfully updated.", movie.getMovieId(),movie.getMovieName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }

    public boolean existsById(Long movieId) {
        boolean exists = movieDAO.existMovieWithId(movieId);
        if (!exists) {
            throw new ResourceNotFoundException("Movie with ID " + movieId + " does not exist.");
        }
        return true;
    }

    public void deleteMovieById(Long userId) {
        if (!movieDAO.existMovieWithId(userId)) {
            throw new ResourceNotFoundException(
                    "Movie with id [%s] not found".
                            formatted(userId));
        }
        movieDAO.deleteMovieById(userId);
    }
}
