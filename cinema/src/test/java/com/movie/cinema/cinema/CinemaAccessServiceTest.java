package com.movie.cinema.cinema;

import com.movie.cinema.CinemaAbstractDaoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    }

    @Test
    void selectCinemaByCinemaName() {
    }

    @Test
    void insertCinema() {
    }

    @Test
    void existCinemaWithName() {
    }

    @Test
    void updateCinema() {
    }
}