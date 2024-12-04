package com.movie.movie.it;

import com.github.javafaker.Faker;

import com.movie.jwt.jwt.JWTUtil;
import com.movie.movie.movie.MovieDTO;
import com.movie.movie.movie.MovieRegistrationRequest;
import com.movie.movie.movie.MovieUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;



/**
 * @author DMITRII LEVKIN on 01/12/2024
 * @project Movie-Reservation-System
 */

@AutoConfigureWebTestClient(timeout = "PT36S")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class MovieServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private String validToken;

    private static String MOVIE_PATH = "/api/v1/movies";

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.issueToken("username", Map.of("role", "ROLE_ADMIN"));
        log.info("validToken = : {}", validToken);
    }

    @Test
    void canRegisterNewMovie() {
        Faker faker = new Faker();
        String name = faker.funnyName().name();
        MovieRegistrationRequest newMovieRequest = new MovieRegistrationRequest(
                name,
                2024,
                "Test Country",
                "Drama",
                "testingTest"
        );

        webTestClient.post()
                .uri("/api/v1/movies")
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(newMovieRequest)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void canUpdateMovie() {

        Faker faker = new Faker();
        String name = faker.funnyName().name();


        MovieRegistrationRequest newMovieRequest = new MovieRegistrationRequest(
                name,
                2024,
                "TestCountry",
                "Drama",
                "testingTest"
        );


        webTestClient.post()
                .uri("/api/v1/movies")
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(newMovieRequest)
                .exchange()
                .expectStatus().isCreated();


    List<MovieDTO> allMovies = webTestClient.get()
            .uri(MOVIE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer " + validToken)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<MovieDTO>() {
            }).returnResult()
            .getResponseBody();

    long movieId = allMovies.stream()
            .filter(user -> user.movieName().equals(name))
            .map(MovieDTO::movieId)
            .findFirst()
            .orElseThrow();


        String newName = faker.funnyName().name();
        int newReleaseYear = 2025;
        String newCountry = "anyCountry";
        String newGenre = "Comedy";
        String newDescription = "TestingTest";

        MovieUpdateRequest movieUpdateRequest = new MovieUpdateRequest(
                newName,
                newReleaseYear,
                newCountry,
                newGenre,
                newDescription
        );


        webTestClient.put()
                .uri(MOVIE_PATH + "/{movieId}", movieId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(movieUpdateRequest), MovieUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();


        MovieDTO movieUpdate = webTestClient.get()
                .uri(MOVIE_PATH + "/{movieId}", movieId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieDTO.class)
                .returnResult()
                .getResponseBody();


        MovieDTO expectedMovie = new MovieDTO(
                movieId,
                newName,
                newReleaseYear,
                newCountry,
                newGenre,
                newDescription
        );

        assertThat(movieUpdate).isEqualTo(expectedMovie);
    }
}
