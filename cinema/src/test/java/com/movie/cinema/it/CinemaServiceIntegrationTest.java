package com.movie.cinema.it;

import com.github.javafaker.Faker;
import com.movie.cinema.CinemaApp;

import com.movie.cinema.cinema.*;
import com.movie.jwt.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void canUpdateCinema() {
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



        List<CinemaDTO> allCinema = webTestClient.get()
                .uri(CINEMA_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CinemaDTO>() {
                }).returnResult()
                .getResponseBody();
        log.info("allSchedules:{}",allCinema);

        long cinemaId = allCinema.stream()

                .map(CinemaDTO::cinemaId)
                .findFirst()
                .orElseThrow();


       String newLocation= "Tokyo";
       String newCinemaLocation = "NewTokyo";

        CinemaUpdateRequest cinemaUpdateRequest = new CinemaUpdateRequest(
                newLocation,newCinemaLocation
        );


        webTestClient.put()
                .uri(CINEMA_PATH + "/{cinemaId}", cinemaId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(cinemaUpdateRequest), CinemaUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        CinemaDTO cinemaUpdate = webTestClient.get()
                .uri(CINEMA_PATH + "/{cinemaId}", cinemaId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CinemaDTO.class)
                .returnResult()
                .getResponseBody();


        CinemaDTO cinemaEx = new CinemaDTO(
                cinemaId,newLocation,newCinemaLocation
        );

        assertThat(cinemaUpdate).isEqualTo(cinemaEx);

    }

    @Test
    void canDeleteCinema() {

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

        List<CinemaDTO> allCinema = webTestClient.get()
                .uri(CINEMA_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CinemaDTO>() {
                }).returnResult()
                .getResponseBody();


        long cinemaId = allCinema.stream()
                .map(CinemaDTO::cinemaId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(CINEMA_PATH + "/{cinemaId}", cinemaId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isOk();

        CinemaDTO cinemaUpdate = webTestClient.get()
                .uri(CINEMA_PATH + "/{cinemaId}", cinemaId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isForbidden()
                .expectBody(CinemaDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(cinemaUpdate).isEqualTo(null);
    }
}

