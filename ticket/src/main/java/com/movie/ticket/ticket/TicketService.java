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
import com.movie.exceptions.AlreadyOccupiedException;
import com.movie.exceptions.HandleRuntimeException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
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


    public TicketService(@Qualifier("ticketJdbc") TicketDAO ticketDAO, TicketDTOMapper ticketDTOMapper, RabbitMqMessageProducer rabbitMqMessageProducer, UserClient userClient, MovieClient movieClient, SeatClient seatClient, ScheduleClient scheduleClient) {
        this.ticketDAO = ticketDAO;

        this.ticketDTOMapper = ticketDTOMapper;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.userClient = userClient;
        this.movieClient = movieClient;
        this.seatClient = seatClient;
        this.scheduleClient = scheduleClient;
    }


    public List<TicketDTO> getAllTickets() {

        return ticketDAO.selectAllTickets().stream()
                .map(ticketDTOMapper)
                .collect(Collectors.toList());
    }

    public void createTicket(TicketRegistrationRequest ticketRegistrationRequest) {

        Ticket ticket = new Ticket();

        // Get the current auth_user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new HandleRuntimeException("Unauthorized user. Please login before proceeding.");
        }
        String username = (String) authentication.getPrincipal();


        // Retrieve user
        UserDTO userDTO = userClient.getUserByUsername(username);
        if (userDTO == null) {
            throw new ResourceNotFoundException("User not found.");
        }


        //fetch seat
        SeatDTO seatDTO = seatClient.getSeatById(ticketRegistrationRequest.seatId());
        if (seatDTO == null) {
            throw new ResourceNotFoundException("Seat not found.");
        }

        // retrieve price
        BigDecimal price = seatClient.getSeatPriceById(ticketRegistrationRequest.seatId());
        if (price == null) {
            throw new ResourceNotFoundException("Price not found for the selected seat.");
        }


        ScheduleDTO scheduleDTO = scheduleClient.getScheduleById(ticketRegistrationRequest.scheduleId());
        if (scheduleDTO == null) {
            throw new ResourceNotFoundException("Schedule not found.");
        }

        // validation

        try {
            boolean movieExists = movieClient.existsById(ticketRegistrationRequest.movieId());
            if (!movieExists) {
                throw new ResourceNotFoundException("Movie with ID " + ticketRegistrationRequest.movieId() + " does not exist.");
            }
        } catch (HandleRuntimeException e) {
            log.error("Authorization error when accessing movie client: {}", e.getMessage());
            throw new HandleRuntimeException("Cannot validate movie existence due to authorization restrictions.");
        } catch (ResourceNotFoundException e) {
            log.error("Movie resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred when accessing movie service: {}", e.getMessage());
            throw new HandleRuntimeException("Unable to process the ticket at this time. Please try again later.");
        }

        Long cinemaId = ticketRegistrationRequest.cinemaId();
        Long seatId = ticketRegistrationRequest.seatId();
        boolean isSeatValid = seatClient.getSeatsByCinema(cinemaId)
                .stream()
                .anyMatch(seat -> seat.seatId().equals(seatId));
        //validation
        if (!isSeatValid) {
            throw new IllegalArgumentException("Invalid seatId for the provided cinemaId.");
        }

        //validation
        if (scheduleDTO.availableSeats() <= 0) {
            throw new AlreadyOccupiedException("Tickets are sold out for this schedule.");
        }

        // validation
        if (seatDTO.isOccupied()) {
            throw new AlreadyOccupiedException("The selected seat is already occupied.");
        }

        //validation
        if (!scheduleDTO.cinemaId().equals(ticketRegistrationRequest.cinemaId())) {
            throw new AlreadyOccupiedException("The schedule does not belong to the selected cinema.");
        }


        // decrease available seats
        scheduleClient.decreaseAvailableSeats(ticketRegistrationRequest.scheduleId());

        // Mark the seat
        seatClient.updateSeatOccupation(ticketRegistrationRequest.seatId(), true);
        String movieName = movieClient.getMovieNameById(ticketRegistrationRequest.movieId());

        String startTime = scheduleClient.getStartTime(ticketRegistrationRequest.scheduleId());

        ticket.setUserId(userDTO.userId());
        ticket.setMovieId(ticketRegistrationRequest.movieId());
        ticket.setCinemaId(ticketRegistrationRequest.cinemaId());
        ticket.setSeatId(ticketRegistrationRequest.seatId());
        ticket.setScheduleId(ticketRegistrationRequest.scheduleId());
        ticket.setPrice(price);
        ticket.setDate(new Date());

        log.info("NEW TICKET: {}", ticket);

        ticketDAO.createOneTicket(ticket);
        NotificationRequest notificationRequest = new NotificationRequest(
                ticket.getTicketId(),
                "Ticket Purchased Successfully",
                String.format("Dear %s, your ticket for the movie '%s' has been successfully booked." +
                        "The price is '%s'.The start time is '%s'. " +
                        "Enjoy the movie!",username,movieName,price,startTime)
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
    public void updateTicket(Long ticketId, TicketUpdateRequest ticketUpdateRequest) {
        // Authentication check to ensure the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new HandleRuntimeException("Unauthorized user. Please login before proceeding.");
        }
        String username = (String) authentication.getPrincipal();

        // Fetch the seat details
        SeatDTO seatDTO = seatClient.getSeatById(ticketUpdateRequest.seatId());
        Ticket ticket = ticketDAO.selectTicketById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with [%s] not found.".formatted(ticketId)));

        Long oldSeatId = ticket.getSeatId();


        if (!ticket.getScheduleId().equals(ticketUpdateRequest.scheduleId())) {
            throw new IllegalArgumentException("Schedule ID cannot be changed.");
        }

        boolean changes = false;

        // Update movie ID if it's different
        if (ticketUpdateRequest.movieId() != null && !ticketUpdateRequest.movieId().equals(ticket.getMovieId())) {
            ticket.setMovieId(ticketUpdateRequest.movieId());
            changes = true;
        }

        // Update cinema ID if it's different
        if (ticketUpdateRequest.cinemaId() != null && !ticketUpdateRequest.cinemaId().equals(ticket.getCinemaId())) {
            ticket.setCinemaId(ticketUpdateRequest.cinemaId());
            changes = true;
        }

        // Check if the seat ID needs to be updated
        if (ticketUpdateRequest.seatId() != null && !ticketUpdateRequest.seatId().equals(ticket.getSeatId())) {
            if (seatDTO == null) {
                throw new ResourceNotFoundException("Seat not found.");
            }
            if (seatDTO.isOccupied()) {
                throw new AlreadyOccupiedException("The selected seat is already occupied.");
            }
            ticket.setSeatId(ticketUpdateRequest.seatId());
            changes = true;
        }

        Long cinemaId = ticketUpdateRequest.cinemaId();
        Long seatId = ticketUpdateRequest.seatId();
        boolean isSeatValid = seatClient.getSeatsByCinema(cinemaId)
                .stream()
                .anyMatch(seat -> seat.seatId().equals(seatId));
        //validation

        if (!isSeatValid) {
            throw new IllegalArgumentException("Invalid seatId for the provided cinemaId.");
        }

        // Update price if it has changed
        if (ticketUpdateRequest.price() != null && !ticketUpdateRequest.price().equals(ticket.getTicketId())) {
            ticket.setPrice(ticketUpdateRequest.price());
            changes = true;
        }

        // Update date if it has changed
        if (ticketUpdateRequest.date() != null && !ticketUpdateRequest.date().equals(ticket.getDate())) {
            ticket.setDate(ticketUpdateRequest.date());
            changes = true;
        }

        // Throw exception if no changes were made
        if (!changes) {
            throw new ResourceNotFoundException("No changes were made.");
        }

        // Update the seat occupation
        if (oldSeatId != null && !oldSeatId.equals(ticketUpdateRequest.seatId())) {
            log.info("Old Seat {} changed.", oldSeatId);
            seatClient.updateSeatOccupation(oldSeatId, false);
        }
        seatClient.updateSeatOccupation(ticketUpdateRequest.seatId(), true);

        // Update the ticket record in the database
        ticketDAO.updateTicket(ticket);
    }

    public void deleteTicket(Long ticketId) {
        ticketDAO.deleteTicket(ticketId);
    }

}
