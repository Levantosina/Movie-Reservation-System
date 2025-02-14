package com.movie.seats.it;

import com.github.javafaker.Faker;
import com.movie.common.ScheduleDTO;
import com.movie.common.UserDTO;
import com.movie.exceptions.DefaultExceptionHandler;
import com.movie.exceptions.ResourceNotFoundException;
import com.movie.jwt.jwt.JWTUtil;

import com.movie.seats.SeatApp;

import com.movie.seats.seat.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(classes = SeatApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Import(DefaultExceptionHandler.class)
public class SeatIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private SeatService seatService;
    @MockBean
    private SeatDAO seatDAO;

    private String validToken;

    private static String SEAT_PATH = "api/v1/seats";

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.issueToken("username", Map.of("role", "ROLE_ADMIN"));
        log.info("validToken = : {}", validToken);


    }

    @Test
    void canRegisterNewSeatScheme() {

        SeatRegistrationRequest seatRegistrationRequest = new SeatRegistrationRequest(
                1, "A", "Standard", 1L,true
        );
        webTestClient.post()
                .uri(SEAT_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(seatRegistrationRequest)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void canGetSeatById() {
        long seatId = 23;
        SeatDTO mockSeatDTO = new SeatDTO(seatId, 1, "A", "standard", false);

        when(seatService.getSeat(seatId)).thenReturn(mockSeatDTO);

        webTestClient.get()
                .uri(SEAT_PATH + "/" + seatId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SeatDTO.class)
                .value(seat -> {
                    assertThat(seat.seatId()).isEqualTo(seatId);
                    assertThat(seat.seatNumber()).isEqualTo(1);
                });
    }

    @Test
    void cannotGetSeatWhenNotFound() {
        Long nonExistentSeatId = 999L;

        when(seatService.getSeat(nonExistentSeatId))
                .thenThrow(new ResourceNotFoundException("Seat with id [%s] not found".formatted(nonExistentSeatId)));

        webTestClient.get()
                .uri(SEAT_PATH + "/" + nonExistentSeatId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isNotFound() // Expect 404 Not Found
                .expectBody(String.class)
                .value(response -> {
                    assertThat(response).contains("Seat with id [999] not found");
                });
    }


    @Test
    void canUpdateTicket() {

        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest(
                265, "D", "VIP", 1L,false
        );

        webTestClient.put()
                .uri(SEAT_PATH + "/{seatId}", 1L)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(seatUpdateRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void canDeleteTicket() {

        Seat seat = new Seat(1L,234, "C", "Standard", 1L,false);
        when(seatDAO.selectSeatById(1L)).thenReturn(Optional.of(seat));

        webTestClient.delete()
                .uri(SEAT_PATH + "/{seatId}", 1L)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk();
    }
}
