package com.movie.users.it;

import com.github.javafaker.Faker;
import com.movie.common.AuthenticationRequest;
import com.movie.common.UserDTO;
import com.movie.users.AbstractDaoUnitTest;
import com.movie.users.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author DMITRII LEVKIN on 18/11/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminServiceIntegrationTest extends AbstractDaoUnitTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    private static String ADMIN_PATH = "/api/v1/admin";
    private static String jwtToken;

    @BeforeEach
    void setUp() {

        String email = "admin@gmail.com";
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Admin", "Test", email, "password", "ROLE_ADMIN"
        );

        User currentAdmin = new User();
        currentAdmin.setEmail(email);
        currentAdmin.setRole(Role.ROLE_ADMIN);
        adminService.registerAdministrator(adminRegistrationRequest, currentAdmin);

        String userEmail = "user1@gmail.com";
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                "USER", "USER", userEmail, "password", "ROLE_USER");
        User currentUser= new User();
        currentUser.setEmail(userEmail);
        currentUser.setRole(Role.ROLE_USER);
        userService.registerUser(userRegistrationRequest);
    }

    @Test
    void canRegisterNewAdmin() {
        Faker faker =new Faker();
        String adminUsername = "admin@gmail.com";
        String adminPassword = "password";

        AuthenticationRequest loginRequest = new AuthenticationRequest(adminUsername, adminPassword);

        jwtToken = webTestClient.post()
                .uri("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Admin", "Admin", "newbla@gmail.com", "password", "ROLE_ADMIN"
        );

        webTestClient.post()
                .uri(ADMIN_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + jwtToken)
                .body(Mono.just(adminRegistrationRequest), AdminRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<UserDTO> allUsers = webTestClient.get()
                .uri(ADMIN_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {})
                .returnResult()
                .getResponseBody();

        long adminId = allUsers.stream()
                .filter(admin -> admin.email().equals(adminRegistrationRequest.email()))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Admin not found"));

        UserDTO expectedAdmin = new UserDTO(adminId, adminRegistrationRequest.firstName(),
                adminRegistrationRequest.lastName(), adminRegistrationRequest.email(),
                adminRegistrationRequest.roleName());
        assertThat(allUsers).contains(expectedAdmin);

        webTestClient.get()
                .uri(ADMIN_PATH + "/{id}", adminId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(expectedAdmin);
    }
    @Test
    void canUpdateUserRole() {
        String adminUsername = "admin@gmail.com";
        String adminPassword = "password";


        AuthenticationRequest loginRequest = new AuthenticationRequest(adminUsername, adminPassword);

        String jwtToken = webTestClient.post()
                .uri("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        RoleRequestDTO roleRequest = new RoleRequestDTO("ROLE_ADMIN");
        String userEmailToUpdate = "user1@gmail.com";

        webTestClient.post()
                .uri("/api/v1/admin/users/{email}/roles", userEmailToUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + jwtToken)
                .body(Mono.just(roleRequest), RoleRequestDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("User role updated successfully");

    }

}