package com.movie.users.it;

import com.github.javafaker.Faker;
import com.movie.common.UserDTO;
import com.movie.users.AbstractDaoUnitTest;
import com.movie.users.users.UserRegistrationRequest;
import com.movie.users.users.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 17/11/2024
 * @project Movie-Reservation-System
 */
@AutoConfigureWebTestClient(timeout = "PT36S")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest extends AbstractDaoUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    private static String USER_PATH = "/api/v1/users";

    private static  String jwtToken;

    @Test
    void canRegisterNewUser() {
        Faker faker =new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password"

        );

        jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        List<UserDTO> allUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {
                })
                .returnResult()
                .getResponseBody();

        long userId = allUsers.stream()
                .filter(user -> user.email().equals(email))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow();

        UserDTO expectedUser= new UserDTO(
                userId,
                firstName,
                lastName,
                email,
                role
        );

        assertThat(allUsers).contains(expectedUser);

        webTestClient.get()
                .uri(USER_PATH + "/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UserDTO>() {
                })
                .isEqualTo(expectedUser);

    }

    @Test
    void canUpdateUser(){


        String firstName = "Test";
        String lastName = "Test";
        String email = "test" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password"

        );

        jwtToken=webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);;


        List<UserDTO> allUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {
                }).returnResult()
                .getResponseBody();


        long userId = allUsers.stream()
                .filter(user -> user.email().equals(email))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow();

        String firstNameUpdated="TestUpdated";

        UserUpdateRequest userUpdateRequest=new UserUpdateRequest(
                firstNameUpdated,null,null
        );

        webTestClient.put()
                .uri(USER_PATH + "/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(MediaType.APPLICATION_JSON
                )
                .body(Mono.just(userUpdateRequest), UserUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        UserDTO updatedUser = webTestClient.get()
                .uri(USER_PATH + "/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();

        UserDTO expected=new UserDTO(
                userId,
                firstNameUpdated,
                lastName,
                email,
                role);

        assertThat(updatedUser).isEqualTo(expected);
    }

    @Test
    void canDeleteUser() {

        String firstName = "Test";
        String lastName = "Test";
        String email = "test" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password"

        );




        jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // Get all users and find User 1's ID
        List<UserDTO> allUsers = webTestClient.get()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {})
                .returnResult()
                .getResponseBody();

        long userId = allUsers.stream()
                .filter(user -> user.email().equals(email))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow();

        // User 1 deletes their own account using their JWT token
        webTestClient.delete()
                .uri(USER_PATH + "/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

    }




    @Test
    void cannotGetNonExistentUser() {

        Faker faker =new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password"

        );

        long nonExistentUserId = 99L;


        String jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        assertThat(jwtToken).isNotNull().isNotEmpty();


        webTestClient.get()
                .uri(USER_PATH + "/{userId}", nonExistentUserId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void cannotRegisterUserWithInvalidData() {
        UserRegistrationRequest invalidUser = new UserRegistrationRequest(
                "",
                "",
                "invalid-email",
                ""
        );

        webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUser), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void cannotRegisterUserWithDuplicateEmail() {

        String email = "test@gmail.com";
        UserRegistrationRequest userRegistrationRequest1 = new UserRegistrationRequest(
                "Test", "Test", email, "password");
        UserRegistrationRequest userRegistrationRequest2 = new UserRegistrationRequest(
                "Test2", "Test2", email, "password");

        webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest1), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest2), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}