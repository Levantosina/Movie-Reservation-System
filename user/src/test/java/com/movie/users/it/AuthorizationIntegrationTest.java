package com.movie.users.it;

import com.github.javafaker.Faker;
import com.movie.common.AuthenticationRequest;
import com.movie.common.AuthenticationResponse;
import com.movie.common.UserDTO;
import com.movie.jwt.jwt.JWTUtil;
import com.movie.users.users.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author DMITRII LEVKIN on 17/11/2024
 * @project Movie-Reservation-System
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;
    private static String AUTHENTICATION_PATH = "/api/v1/auth";
    private static String USER_PATH = "/api/v1/users";


    @Test
    void login(){
        Faker faker =new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";
        String password="password";
        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                password,
                role
        );

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );
        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest),UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest),AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>(){})
                .returnResult();

        String token = Objects.requireNonNull(result.getResponseHeaders().get(HttpHeaders.AUTHORIZATION)).getFirst();

        AuthenticationResponse authenticationResponse = result.getResponseBody();
        UserDTO userDTO = authenticationResponse.userDTO();

        assertThat(jwtUtil.isTokenValid(token,userDTO.email())).isTrue();

        assertThat(userDTO.firstName()).isEqualTo(firstName);
        assertThat(userDTO.lastName()).isEqualTo(lastName);
        assertThat(userDTO.email()).isEqualTo(email);
        assertThat(userDTO.role_name()).isEqualTo(role);

    }
}
