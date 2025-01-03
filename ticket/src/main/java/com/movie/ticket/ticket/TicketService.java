package com.movie.ticket.ticket;


import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.notification.NotificationRequest;
import com.movie.client.scheduleClient.ScheduleClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.client.userClient.UserClient;
import com.movie.common.MovieDTO;
import com.movie.common.ScheduleDTO;
import com.movie.common.SeatDTO;
import com.movie.common.UserDTO;
import com.movie.exceptions.AlreadyOccupiedException;
import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@Service
@Slf4j
public class TicketService {

    private final TicketDAO ticketDAO;


    private final TicketDTOMapper ticketDTOMapper;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    private final UserClient userClient;

    private final MovieClient movieClient;

    private final SeatClient seatClient;
    private final ScheduleClient scheduleClient;




    public TicketService(@Qualifier("ticketJdbc")TicketDAO ticketDAO, TicketDTOMapper ticketDTOMapper, RabbitMqMessageProducer rabbitMqMessageProducer, UserClient userClient, MovieClient movieClient, SeatClient seatClient, ScheduleClient scheduleClient) {
        this.ticketDAO = ticketDAO;

        this.ticketDTOMapper = ticketDTOMapper;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.userClient = userClient;
        this.movieClient = movieClient;
        this.seatClient = seatClient;
        this.scheduleClient = scheduleClient;
    }


    public List<TicketDTO> getAllTickets() {

        return  ticketDAO.selectAllTickets().stream()
                .map(ticketDTOMapper)
                .collect(Collectors.toList());
    }

    public void createTicket(TicketRegistrationRequest ticketRegistrationRequest){

        Ticket ticket = new Ticket();

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDTO userDTO = userClient.getUserByUsername(username);
        if (userDTO == null) {
            throw new ResourceNotFoundException("User not found.");
        }


        SeatDTO seatDTO = seatClient.getSeatById(ticketRegistrationRequest.seatId());
        if (seatDTO == null) {
            throw new ResourceNotFoundException("Seat not found.");
        }

        ScheduleDTO scheduleDTO = scheduleClient.getScheduleById(ticketRegistrationRequest.scheduleId());
        if (scheduleDTO == null) {
            throw new ResourceNotFoundException("Schedule not found.");
        }

        MovieDTO movieDTO = movieClient.getMovieById(ticketRegistrationRequest.movieId());
        if (movieDTO == null) {
            throw new ResourceNotFoundException("Movie not found.");
        }

        Long userId = userDTO.userId();

        if (scheduleDTO.availableSeats() <= 0) {
            throw new AlreadyOccupiedException("Tickets are sold out for this schedule.");
        }

        if(seatDTO.isOccupied())
            throw new AlreadyOccupiedException("The selected seat is already occupied.");

        if (!scheduleDTO.cinemaId().equals(ticketRegistrationRequest.cinemaId())) {
            throw new AlreadyOccupiedException("The schedule does not belong to the selected cinema.");
        }


        scheduleClient.decreaseAvailableSeats(ticketRegistrationRequest.scheduleId());
        seatClient.updateSeatOccupation(ticketRegistrationRequest.seatId(), true);



        ticket.setUserId(userId);
        ticket.setMovieId(ticketRegistrationRequest.movieId());
        ticket.setCinemaId(ticketRegistrationRequest.cinemaId());
        ticket.setSeatId(ticketRegistrationRequest.seatId());
        ticket.setScheduleId(ticketRegistrationRequest.scheduleId());
        ticket.setPrice(ticketRegistrationRequest.price());
        ticket.setDate(ticketRegistrationRequest.date());
        log.info("TICKET:{}", ticket);
        ticketDAO.createOneTicket(ticket);


        NotificationRequest notificationRequest = new NotificationRequest(
                ticket.getTicketId(), "New Ticket created", "The ticket has been created."
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }

}
