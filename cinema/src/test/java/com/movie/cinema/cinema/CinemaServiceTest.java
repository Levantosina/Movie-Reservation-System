package com.movie.cinema.cinema;

import com.movie.amqp.RabbitMqMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

/**
 * @author DMITRII LEVKIN on 04/12/2024
 * @project Movie-Reservation-System
 */
@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    private CinemaService underTest;
    @Mock
    private CinemaDAO cinemaDAO;

    private final CinemaDTOMapper cinemaDTOMapper = new CinemaDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer ;

    @BeforeEach
    void setUp() {

        underTest = new CinemaService(cinemaDAO,cinemaDTOMapper,rabbitMqMessageProducer);

    }

    @Test
    void getAllCinemas() {
        underTest.getAllCinemas();
        verify(cinemaDAO).selectAllCinemas();
    }

    @Test
    void getCinema() {
    }

    @Test
    void getCinemaByName() {
    }

    @Test
    void getCinemaIdByName() {
    }

    @Test
    void registerCinema() {
    }

    @Test
    void loadCinemasFromCSV() {
    }

    @Test
    void getCinemaMap() {
    }
}