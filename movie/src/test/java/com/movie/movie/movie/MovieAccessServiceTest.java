package com.movie.movie.movie;


import com.movie.movie.MovieAbstractDaoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author DMITRII LEVKIN on 26/11/2024
 * @project Movie-Reservation-System
 */
class MovieAccessServiceTest extends MovieAbstractDaoUnitTest {

    private MovieAccessService underTest;

    private final MovieRowMapper movieRowMapper = new MovieRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new MovieAccessService(getJdbcTemplate(),
                movieRowMapper);
    }

    @Test
    void selectAllMovies() {
        Movie movie = Movie.builder()
                .movieName(FAKER.random().hex())
                .year(2024)
                .country("France")
                .genre("Action")
                .description("New movie")
                .build();
        underTest.insertMovie(movie);
        List<Movie> actual = underTest.selectAllMovies();
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectMovieById() {
        String country = "Finland";
        Movie movie = Movie.builder()
                .movieName(FAKER.random().hex())
                .year(2022)
                .country(country)
                .genre("Action")
                .description("Old movie")
                .build();
        underTest.insertMovie(movie);


        long movieId = underTest.selectAllMovies()
                .stream()
                .filter(c -> c.getCountry().equals(country))
                .map(Movie::getMovieId)
                .findFirst()
                .orElseThrow();

        Optional<Movie> actual = underTest.selectMovieById((long) movieId);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getMovieId()).isEqualTo(movieId);
            assertThat(c.getMovieName()).isEqualTo(movie.getMovieName());
            assertThat(c.getYear()).isEqualTo(movie.getYear());
            assertThat(c.getCountry()).isEqualTo(movie.getCountry());
            assertThat(c.getGenre()).isEqualTo(movie.getGenre());
            assertThat(c.getDescription()).isEqualTo(movie.getDescription());
        });

    }

    @Test
    void selectMovieByName() {
        String movieName = FAKER.random().hex();
        Movie movie = Movie.builder()
                .movieName(movieName)
                .year(2024)
                .country("France")
                .genre("Action")
                .description("New movie")
                .build();
        underTest.insertMovie(movie);


        String movieNameTest = underTest.selectAllMovies()
                .stream()
                .map(Movie::getMovieName)
                .filter(cMovieName -> cMovieName.equals(movieName))
                .findFirst()
                .orElseThrow();

        Optional<Movie> actual = underTest.selectMovieByName(movieNameTest);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getMovieName()).isEqualTo(movie.getMovieName());
            assertThat(c.getYear()).isEqualTo(movie.getYear());
            assertThat(c.getCountry()).isEqualTo(movie.getCountry());
            assertThat(c.getGenre()).isEqualTo(movie.getGenre());
            assertThat(c.getDescription()).isEqualTo(movie.getDescription());
        });
    }


    @Test
    void insertMovie() {
        String movieName = FAKER.random().hex();
        Movie movie = Movie.builder()
                .movieName(movieName)
                .year(2024)
                .country("France")
                .genre("Action")
                .description("New movie")
                .build();
        underTest.insertMovie(movie);

        Optional<Movie> actual = underTest.selectMovieByName(movieName);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getMovieName()).isEqualTo(movie.getMovieName());
            assertThat(c.getYear()).isEqualTo(movie.getYear());
            assertThat(c.getCountry()).isEqualTo(movie.getCountry());
            assertThat(c.getGenre()).isEqualTo(movie.getGenre());
            assertThat(c.getDescription()).isEqualTo(movie.getDescription());
        });

    }

    @Test
    void updateMovie() {
        String movieName = FAKER.random().hex();
        Movie movie = Movie.builder()
                .movieName(movieName)
                .year(2024)
                .country("France")
                .genre("Action")
                .description("New movie")
                .build();
        underTest.insertMovie(movie);

        long movieId = underTest.selectAllMovies()
                .stream()
                .filter(c -> c.getMovieName().equals(movieName))
                .map(Movie::getMovieId)
                .findFirst()
                .orElseThrow();

        String updateMovieName = "TestMovie";
        Integer updateYear = 2024;
        String updateMovieCountry = "USA";
        String updateMovieGenre = "Fiction";
        String updateMovieDescription = "Test";


        movie.setMovieName(updateMovieName);
        movie.setYear(updateYear);
        movie.setCountry(updateMovieCountry);
        movie.setGenre(updateMovieGenre);
        movie.setDescription(updateMovieDescription);

        underTest.updateMovie(movie);

        Optional<Movie> updateMovie = underTest.selectMovieById(movieId);
        assertThat(updateMovie).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getMovieName()).isEqualTo(updateMovieName);
            assertThat(u.getYear()).isEqualTo(updateYear);
            assertThat(u.getCountry()).isEqualTo(updateMovieCountry);
            assertThat(u.getGenre()).isEqualTo(updateMovieGenre);
            assertThat(u.getDescription()).isEqualTo(updateMovieDescription);
        });
    }
}
