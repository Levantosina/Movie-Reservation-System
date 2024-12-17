package com.movie.cinema.it;

import com.github.javafaker.Faker;
import com.movie.cinema.CinemaApp;

import com.movie.cinema.cinema.CinemaDAO;
import com.movie.cinema.cinema.CinemaRegistrationRequest;
import com.movie.cinema.cinema.CinemaService;
import com.movie.jwt.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.Map;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 09/12/2024
 * @project Movie-Reservation-System
 */

@SpringBootTest(classes = CinemaApp.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CinemaServiceIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private String validToken;

    private static String CINEMA_PATH = "/api/v1/cinemas";
    @Mock
    private  static  CinemaService cinemaService;
    @Mock
    private static CinemaDAO cinemaDAO;


    @BeforeEach
    void setUp() {
        validToken = jwtUtil.issueToken("username", Map.of("role", "ROLE_ADMIN"));
        log.info("validToken = : {}", validToken);
    }

    @Test
    void canRegisterNewCinema() {
        Faker faker = new Faker();
        String cinemaName = faker.name().lastName();
        String cinemaLocation = faker.name().firstName();

        CinemaRegistrationRequest cinemaRegistrationRequest = new CinemaRegistrationRequest(
                cinemaName,
                cinemaLocation
        );

        webTestClient.post()
                .uri(CINEMA_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(cinemaRegistrationRequest)
                .exchange()
                .expectStatus().isCreated();
    }
}

