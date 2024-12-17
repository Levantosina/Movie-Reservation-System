package com.movie.seats.it;

import com.movie.jwt.jwt.JWTUtil;

import com.movie.seats.SeatApp;
import com.movie.seats.exception.GlobalExceptions;
import com.movie.seats.exception.SeatNotFoundException;
import com.movie.seats.seat.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;



import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(classes = SeatApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Import(GlobalExceptions.class)
public class SeatIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private SeatService seatService;
    @Autowired
    private SeatDAO seatDAO;

    @Autowired
    SeatDTOMapper seatDTOMapper;

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
                .thenThrow(new SeatNotFoundException("Seat with id [%s] not found".formatted(nonExistentSeatId)));

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
}
