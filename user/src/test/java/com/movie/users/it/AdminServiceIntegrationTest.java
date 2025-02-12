package com.movie.users.it;

import com.github.javafaker.Faker;
import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;
import com.movie.common.AuthenticationRequest;
import com.movie.common.UserDTO;
import com.movie.users.AbstractDaoUnitTest;
import com.movie.users.users.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author DMITRII LEVKIN on 18/11/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AdminServiceIntegrationTest extends AbstractDaoUnitTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    private static String ADMIN_PATH = "/api/v1/admin";

    @Test
    void canRegisterNewAdminAndPublishNotification() {

        Faker faker = new Faker();
        String firstName = "Test";
        String lastName = "Test";
        String email = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String role = "ROLE_ADMIN";
        String password = "password";

        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                firstName, lastName, email, password, role
        );

        User currentAdmin = new User();
        currentAdmin.setEmail(email);
        currentAdmin.setRole(Role.ROLE_ADMIN);


        RabbitMqMessageProducer rabbitMqMessageProducerMock = mock(RabbitMqMessageProducer.class);
        ReflectionTestUtils.setField(adminService, "rabbitMqMessageProducer", rabbitMqMessageProducerMock);


        adminService.registerAdministrator(adminRegistrationRequest, currentAdmin);

        verify(rabbitMqMessageProducerMock, times(1)).publish(
                any(NotificationRequest.class),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );

        AuthenticationRequest loginRequest = new AuthenticationRequest(email, password);
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


        String email1 = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        AdminRegistrationRequest adminRegistrationRequest1 = new AdminRegistrationRequest(
                "Admin", "Admin", email1, "password", "ROLE_ADMIN"
        );

        // Register the new admin
        webTestClient.post()
                .uri(ADMIN_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + jwtToken)
                .body(Mono.just(adminRegistrationRequest1), AdminRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();


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
                .filter(admin -> admin.email().equals(adminRegistrationRequest1.email()))
                .map(UserDTO::userId)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Admin not found"));

        UserDTO expectedAdmin = new UserDTO(adminId, adminRegistrationRequest1.firstName(),
                adminRegistrationRequest1.lastName(), adminRegistrationRequest1.email(),
                adminRegistrationRequest1.roleName());


        assertThat(allUsers).contains(expectedAdmin);


        webTestClient.get()
                .uri(ADMIN_PATH + "/user/{id}", adminId)
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

        String email = "admin@gmail.com";
        String password = "password";
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Admin", "Test", email, "password", "ROLE_ADMIN"
        );

        User currentAdmin = new User();
        currentAdmin.setEmail(email);
        currentAdmin.setRole(Role.ROLE_ADMIN);
        adminService.registerAdministrator(adminRegistrationRequest, currentAdmin);

        String userEmail = "user1@gmail.com";
        String userPassword = "password";

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                "USER", "USER", userEmail, userPassword);
        User currentUser= new User();
        currentUser.setEmail(userEmail);
        currentUser.setRole(Role.ROLE_USER);
        userService.registerUser(userRegistrationRequest);



        AuthenticationRequest loginRequest = new AuthenticationRequest(email, password);

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

        webTestClient.post()
                .uri("/api/v1/admin/users/{email}/roles", userEmail)
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