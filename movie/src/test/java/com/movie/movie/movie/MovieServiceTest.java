package com.movie.movie.movie;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author DMITRII LEVKIN on 27/11/2024
 * @project Movie-Reservation-System
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private MovieService underTest;
    @Mock
    private MovieDAO movieDAO;

    private final MovieDTOMapper movieDTOMapper = new MovieDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer ;

    @BeforeEach
    void setUp() {

        underTest = new MovieService(movieDAO,movieDTOMapper,rabbitMqMessageProducer);

    }

    @Test
    void getAllMovies() {
        underTest.getAllMovies();
        verify(movieDAO).selectAllMovies();
    }

    @Test
    void getMovieById() {
        long movie_id = 33;
        Movie movie = new Movie(movie_id,"TestMovie",2023,"USA","Action","Test");

        when(movieDAO.selectMovieById(movie_id)).thenReturn(Optional.of(movie));

        MovieDTO expected = movieDTOMapper.apply(movie);
        MovieDTO actual = underTest.getMovieById(movie_id);
        assertThat(actual).isEqualTo(expected);
        verify(movieDAO).selectMovieById(movie_id);
    }

    @Test
    void registerNewMovie() {
        String movieName = "TestNewMovie";
        when(movieDAO.selectMovieByName(movieName)).thenReturn(Optional.empty());

        MovieRegistrationRequest registrationRequest = new MovieRegistrationRequest(
                movieName,
                2022,
                "Greece",
                "Action",
                "TestRegistration"
        );

       underTest.registerNewMovie(registrationRequest);

        ArgumentCaptor<Movie> movieArgumentCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieDAO).insertMovie(movieArgumentCaptor.capture());
        Movie capturedMovie = movieArgumentCaptor.getValue();

        assertThat(capturedMovie.getMovieId()).isNull();
        assertThat(capturedMovie.getMovieName()).isEqualTo(registrationRequest.movieName());
        assertThat(capturedMovie.getYear()).isEqualTo(registrationRequest.year());
        assertThat(capturedMovie.getCountry()).isEqualTo(registrationRequest.country());
        assertThat(capturedMovie.getGenre()).isEqualTo(registrationRequest.genre());
        assertThat(capturedMovie.getDescription()).isEqualTo(registrationRequest.description());

        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
    }

    @Test
    void updateMovie() {
        Long movieId = 4L;
        String movieName = "MovieName";
       Movie movie= new Movie(
               movieId,
               movieName,
                2022,
                "Greece",
                "Action",
                "TestRegistration"
        );

        when(movieDAO.selectMovieById(movieId)).thenReturn(Optional.of(movie));


        MovieUpdateRequest movieUpdateRequest = new MovieUpdateRequest(
                "NewTestMovie",
                2026,
                "Greece",
                "Drama",
                "TestNewMovie");

        underTest.updateMovie(movieId, movieUpdateRequest);

        ArgumentCaptor<Movie> movieArgumentCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieDAO).updateMovie(movieArgumentCaptor.capture());
        Movie updatedMovie = movieArgumentCaptor.getValue();

        assertThat(updatedMovie.getMovieId()).isEqualTo(movieId);
        assertThat(updatedMovie.getMovieName()).isEqualTo(movieUpdateRequest.movieName());
        assertThat(updatedMovie.getYear()).isEqualTo(movieUpdateRequest.year());
        assertThat(updatedMovie.getCountry()).isEqualTo(movieUpdateRequest.country());
        assertThat(updatedMovie.getGenre()).isEqualTo(movieUpdateRequest.genre());
        assertThat(updatedMovie.getDescription()).isEqualTo(movieUpdateRequest.description());


    }
}
