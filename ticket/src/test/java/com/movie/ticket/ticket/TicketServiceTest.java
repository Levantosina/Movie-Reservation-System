package com.movie.ticket.ticket;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.notification.NotificationRequest;
import com.movie.client.scheduleClient.ScheduleClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.client.userClient.UserClient;
import com.movie.common.ScheduleDTO;
import com.movie.common.SeatDTO;
import com.movie.common.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private TicketService underTests;

    @Mock
    private TicketDAO ticketDAO;

    private final TicketDTOMapper ticketDTOMapper = new TicketDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer;
    @Mock
    private UserClient userClient;
    @Mock
    private MovieClient movieClient;
    @Mock
    private ScheduleClient scheduleClient;
    @Mock
    private SeatClient seatClient;

    @BeforeEach
    void setUp() {
        underTests = new TicketService(ticketDAO,ticketDTOMapper,rabbitMqMessageProducer,userClient,movieClient,seatClient,scheduleClient);
    }

    @Test
    void getAllTickets() {
        underTests.getAllTickets();
        verify(ticketDAO).selectAllTickets();
    }

    @Test
    void createTicketTest() {

        TicketRegistrationRequest ticketRegistrationRequest = new TicketRegistrationRequest(1L, 1L, 1L, 1L);

        // mocking auth user
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("testUser");


        UserDTO userDTO = new UserDTO(1L, "testUser", "testLastname", "test@example.com", "ROLE_USER");
        when(userClient.getUserByUsername("testUser")).thenReturn(userDTO);

        //mock seat
        SeatDTO seatDTO = new SeatDTO(1L, 2, "A", "VIP", false);

        when(seatClient.getSeatById(1L)).thenReturn(seatDTO);
        when(seatClient.getSeatPriceById(1L)).thenReturn(BigDecimal.valueOf(32.00));
        when(seatClient.getSeatsByCinema(1L)).thenReturn(List.of(seatDTO));

        //mock schedule
        ScheduleDTO scheduleDTO = new ScheduleDTO(1L, LocalDate.parse("2025-12-12"), LocalTime.parse("10:00:00"), LocalTime.parse("12:00:00"),
                50, 1L, 1L);

        when(scheduleClient.getScheduleById(1L)).thenReturn(scheduleDTO);
        when(scheduleClient.getStartTime(1L)).thenReturn("10:00 AM");

        when(movieClient.existsById(1L)).thenReturn(true);
        when(movieClient.getMovieNameById(1L)).thenReturn("Inception");


        underTests.createTicket(ticketRegistrationRequest);


        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketDAO).createOneTicket(ticketCaptor.capture());
        Ticket capturedTicket = ticketCaptor.getValue();

        assertAll(
                () -> assertThat(capturedTicket.getUserId()).isEqualTo(userDTO.userId()),
                () -> assertThat(capturedTicket.getMovieId()).isEqualTo(ticketRegistrationRequest.movieId()),
                () -> assertThat(capturedTicket.getCinemaId()).isEqualTo(ticketRegistrationRequest.cinemaId()),
                () -> assertThat(capturedTicket.getSeatId()).isEqualTo(ticketRegistrationRequest.seatId()),
                () -> assertThat(capturedTicket.getScheduleId()).isEqualTo(ticketRegistrationRequest.scheduleId()),
                () -> assertThat(capturedTicket.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(32.00))
        );

        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
    }


    @Test
    void updateTicket_success() {

        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest(1L, 2L, 1L,
                2L, BigDecimal.valueOf(150.0), new Date());

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("testUser");


        SeatDTO seatDTO = new SeatDTO(2L, 3, "B", "VIP", false);


        when(seatClient.getSeatsByCinema(2L)).thenReturn(List.of(seatDTO));
        when(seatClient.getSeatById(2L)).thenReturn(seatDTO);

        Ticket existingTicket = new Ticket(1L, 1L, 1L, 1L, 1L, 1L,
                BigDecimal.valueOf(100.0), new Date());
        when(ticketDAO.selectTicketById(1L)).thenReturn(Optional.of(existingTicket));

        underTests.updateTicket(1L, ticketUpdateRequest);

        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(seatClient, times(1)).updateSeatOccupation(1L, false);
        verify(seatClient, times(1)).updateSeatOccupation(2L, true);
    }

    @Test
    void deleteTicket() {
        long ticketId = 1;
        when(ticketDAO.existTicketWithId(ticketId)).thenReturn(true);
        underTests.deleteTicket(ticketId);
        verify(ticketDAO).deleteTicket(ticketId);
    }
}