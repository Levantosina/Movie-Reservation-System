package com.movie.cinema.cinema;

import com.movie.cinema.CinemaAbstractDaoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author DMITRII LEVKIN on 04/12/2024
 * @project Movie-Reservation-System
 */
class CinemaAccessServiceTest extends CinemaAbstractDaoUnitTest {

    private CinemaAccessService underTest;
    private final CinemaRowMapper cinemaRowMapper = new CinemaRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CinemaAccessService(getJdbcTemplate(),
                cinemaRowMapper);
    }

    @Test
    void selectAllCinemas() {

        Cinema cinema = Cinema.builder()
                .cinemaName("")
                .cinemaLocation("")
                .build();

        underTest.insertCinema(cinema);
        List<Cinema> actual = underTest.selectAllCinemas();
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCinemaById() {
        String cinemaName = "Dron";
        Cinema cinema = Cinema.builder()
                .cinemaName(cinemaName)
                .cinemaLocation("AnyLocation")
                .build();
        underTest.insertCinema(cinema);

        long id = underTest.selectAllCinemas()
                .stream()
                .filter(c -> c.getCinemaName().equals(cinemaName))
                .map(Cinema::getCinemaId)
                .findFirst()
                .orElseThrow();

        Optional<Cinema> actual = underTest.selectCinemaById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getCinemaId()).isEqualTo(id);
            assertThat(c.getCinemaName()).isEqualTo(cinemaName);
            assertThat(c.getCinemaLocation()).isEqualTo("AnyLocation");
        });
    }

    @Test
    void selectCinemaByCinemaName() {

        String cinemaName = "Drago";
        Cinema cinema = Cinema.builder()
                .cinemaName(cinemaName)
                .cinemaLocation("AnyLocation")
                .build();
        underTest.insertCinema(cinema);

        String cinemaNameTest = underTest.selectAllCinemas()
                .stream()
                .filter(c -> c.getCinemaName().equals(cinemaName))
                .map(Cinema::getCinemaName)
                .findFirst()
                .orElseThrow();

        Optional<Cinema> actual = underTest.selectCinemaByCinemaName(cinemaNameTest);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getCinemaName()).isEqualTo(cinemaName);
            assertThat(c.getCinemaLocation()).isEqualTo("AnyLocation");
        });

    }

    @Test
    void insertCinema() {
        String cinemaName = "Drago";
        String cinemaLocation = "AnyLocation";
        Cinema cinema = Cinema.builder()
                .cinemaName(cinemaName)
                .cinemaLocation(cinemaLocation)
                .build();
        underTest.insertCinema(cinema);

        Optional<Cinema> actual = underTest.selectCinemaByCinemaName(cinemaName);
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getCinemaName()).isEqualTo(cinemaName);
            assertThat(c.getCinemaLocation()).isEqualTo(cinemaLocation);
        });

    }

//    @Test
//    void existCinemaWithName() {
//    }

//    @Test
//    void updateCinema() {
//    }
}