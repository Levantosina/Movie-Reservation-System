package com.movie.users.it;

import com.github.javafaker.Faker;
import com.movie.common.UserDTO;
import com.movie.users.AbstractDaoUnitTest;
import com.movie.users.users.UserRegistrationRequest;
import com.movie.users.users.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
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
                "password",
                role
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

        Faker faker =new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password",
                role
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
    void canDeleteUser(){

        Faker faker =new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role="ROLE_USER";

        String firstName2 = "Test2";
        String lastName2 = "Test2";
        String email2= faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role2="ROLE_USER";

        UserRegistrationRequest userRegistrationRequest1= new UserRegistrationRequest(
                firstName,
                lastName,
                email,
                "password",
                role
        );
        UserRegistrationRequest userRegistrationRequest2= new UserRegistrationRequest(
                firstName2,
                lastName2,
                email2,
                "password",
                role2
        );

        webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest1), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        jwtToken = webTestClient.post()
                .uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest2), UserRegistrationRequest.class)
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
                }).returnResult()
                .getResponseBody();


        long id = allUsers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow();


        webTestClient.delete()
                .uri(USER_PATH + "/{userId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();


        webTestClient.get()
                .uri(USER_PATH + "/{userId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void cannotGetNonExistentUser() {
        long nonExistentUserId = 99L;

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
                "",
                "ROLE_USER"
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
                "Test", "Test", email, "password", "ROLE_USER");
        UserRegistrationRequest userRegistrationRequest2 = new UserRegistrationRequest(
                "Test2", "Test2", email, "password", "ROLE_USER");

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
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody();
    }

}
