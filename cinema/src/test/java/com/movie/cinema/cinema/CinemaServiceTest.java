package com.movie.cinema.cinema;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;
import com.movie.client.seatClient.SeatClient;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStreamReader;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author DMITRII LEVKIN on 04/12/2024
 * @project Movie-Reservation-System
 */
@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    private CinemaService underTest;
    @Mock
    private CinemaDAO cinemaDAO;
    @Mock
    SeatClient seatClient;

    private final CinemaDTOMapper cinemaDTOMapper = new CinemaDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer;

    @BeforeEach
    void setUp() {

        underTest = new CinemaService(cinemaDAO, cinemaDTOMapper, rabbitMqMessageProducer,seatClient);

    }

    @Test
    void getAllCinemas() {
        underTest.getAllCinemas();
        verify(cinemaDAO).selectAllCinemas();
    }

    @Test
    void getCinema() {

        long cinema_id = 12;
        Cinema cinema = new Cinema(cinema_id, "Astral", "Helsinki");

        when(cinemaDAO.selectCinemaById(cinema_id)).thenReturn(Optional.of(cinema));

        CinemaDTO expected = cinemaDTOMapper.apply(cinema);
        CinemaDTO actual = underTest.getCinemaById(cinema_id);
        assertThat(actual).isEqualTo(expected);
        verify(cinemaDAO).selectCinemaById(cinema_id);
    }

    @Test
    void getCinemaByName() {
        String cinemaName = "PerfectWorld";
        Cinema cinema = new Cinema(2L, cinemaName, "NY");

        when(cinemaDAO.selectCinemaByCinemaName(cinemaName)).thenReturn(Optional.of(cinema));

        CinemaDTO expected = cinemaDTOMapper.apply(cinema);
        CinemaDTO actual = underTest.getCinemaByName(cinemaName);
        assertThat(actual).isEqualTo(expected);
        verify(cinemaDAO).selectCinemaByCinemaName(cinemaName);
    }

    @Test
    void registerCinema() {

        String cinemaName = "PerfectWorldTest";
        when(cinemaDAO.selectCinemaByCinemaName(cinemaName)).thenReturn(Optional.empty());

        CinemaRegistrationRequest cinemaRegistrationRequest = new CinemaRegistrationRequest(
                cinemaName,
                "NY"
        );

        underTest.registerCinema(cinemaRegistrationRequest);

        ArgumentCaptor<Cinema> cinemaArgumentCaptor = ArgumentCaptor.forClass(Cinema.class);
        verify(cinemaDAO).insertCinema(cinemaArgumentCaptor.capture());
        Cinema cinemaCaptured = cinemaArgumentCaptor.getValue();

        assertThat(cinemaCaptured.getCinemaId()).isNull();
        assertThat(cinemaCaptured.getCinemaName()).isEqualTo(cinemaRegistrationRequest.cinemaName());
        assertThat(cinemaCaptured.getCinemaLocation()).isEqualTo(cinemaRegistrationRequest.cinemaLocation());

        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
    }


    @ParameterizedTest
    @CsvFileSource(resources = {"/cinemas.csv" }, numLinesToSkip = 1, encoding = "UTF-8" )
    void testWithCsvFileResource(String cinemaName, String city) {
        assertTrue(cinemaName.length() > 2);
        assertTrue(city.length()>2);
    }
}