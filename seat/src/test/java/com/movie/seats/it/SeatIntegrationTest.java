package com.movie.seats.it;

import com.movie.jwt.jwt.JWTUtil;

import com.movie.seats.SeatApp;
import com.movie.seats.seat.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;



import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(classes = SeatApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class SeatIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SeatService seatService;
    @Autowired
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
                1, "A", "Standard", 1L
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
        Long seatId = 1L;
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
        Long nonExistentSeatId = 999L; // Seat ID that doesn't exist in the database
        webTestClient.get()
                .uri(SEAT_PATH + "/" + nonExistentSeatId) // Access endpoint
                .header(AUTHORIZATION, "Bearer " + validToken) // Valid token
                .exchange()
                .expectStatus().isNotFound() // Expect 404 Not Found
                .expectBody(String.class)
                .value(response -> {
                    assertThat(response).isEmpty(); // The body should be null or empty
                });
    }
}
