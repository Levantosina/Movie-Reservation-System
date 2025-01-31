package com.movie.ticket.it;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.scheduleClient.ScheduleClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.client.userClient.UserClient;
import com.movie.common.ScheduleDTO;
import com.movie.common.SeatDTO;
import com.movie.common.UserDTO;
import com.movie.jwt.jwt.JWTUtil;
import com.movie.ticket.TicketAbstractDaoUnitTest;
import com.movie.ticket.TicketApp;
import com.movie.ticket.ticket.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(classes = TicketApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TicketIntegrationTest extends TicketAbstractDaoUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private TicketService ticketService;

    private String validToken;

    private static String TICKET_PATH = "/api/v1/ticket";
    @MockBean
    private UserClient userClient;
    @MockBean
    private ScheduleClient scheduleClient;
    @MockBean
    private SeatClient seatClient;
    @MockBean
    private MovieClient movieClient;
    @MockBean
    private TicketDAO ticketDAO;

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.issueToken("username", Map.of("role", "ROLE_ADMIN"));
        log.info("validToken = : {}", validToken);
    }


    @Test
    void testTicketEndpointBasicResponse() {
        webTestClient.post()
                .uri("/api/v1/ticket")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void canRegisterTicket() {

        TicketRegistrationRequest ticketRegistrationRequest = new TicketRegistrationRequest(
                1L, 1L, 1L, 1L
        );

        when(userClient.getUserByUsername(anyString())).thenReturn(new UserDTO(1L, "testUser", "testLastname", "test@example.com", "ROLE_USER"));
        when(seatClient.getSeatById(1L)).thenReturn(new SeatDTO(1L, 2, "A", "VIP", false));
        when(seatClient.getSeatPriceById(1L)).thenReturn(BigDecimal.TEN);
        when(scheduleClient.getScheduleById(1L)).thenReturn(new ScheduleDTO(1L, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(2), 50, 1L, 1L));
        when(movieClient.existsById(1L)).thenReturn(true);

        webTestClient.post()
                .uri(TICKET_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(ticketRegistrationRequest)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void canUpdateTicket() {

        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest(
                1L, 1L, 1L, 2L, BigDecimal.valueOf(10.0), new Date()
        );

        webTestClient.put()
                .uri(TICKET_PATH + "/{ticketId}", 1L)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(ticketUpdateRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void canDeleteTicket() {

        Ticket ticket = new Ticket(1L,1L, 1L, 1L, 1L, 1L, BigDecimal.valueOf(10.0), new Date());


        when(userClient.getUserByUsername(anyString())).thenReturn(new UserDTO(1L, "testUser", "testLastname", "test@example.com", "ROLE_USER"));
        when(seatClient.getSeatById(1L)).thenReturn(new SeatDTO(1L, 2, "A", "VIP", false));
        when(seatClient.getSeatPriceById(1L)).thenReturn(BigDecimal.TEN);
        when(scheduleClient.getScheduleById(1L)).thenReturn(new ScheduleDTO(1L, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(2), 50, 1L, 1L));
        when(movieClient.existsById(1L)).thenReturn(true);

        when(ticketDAO.selectTicketById(1L)).thenReturn(Optional.of(ticket));

        webTestClient.delete()
                .uri(TICKET_PATH + "/{ticketId}", 1L)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void canCreateAndGetAllTickets() {

        TicketRegistrationRequest ticketRequest =
                new TicketRegistrationRequest(1L, 1L, 1L, 1L);
        webTestClient.post()
                .uri(TICKET_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(ticketRequest)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri(TICKET_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk();
    }
}

