package com.movie.ticket.ticket;

import com.movie.client.cinemaClient.CinemaClient;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.scheduleClient.ScheduleClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.client.userClient.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@Service

public class TicketService {

    private final TicketDAO ticketDAO;
    private final UserClient userClient;
    private final MovieClient movieClient;
    private final CinemaClient cinemaClient;
    private final SeatClient seatClient;
    private final ScheduleClient scheduleClient;


    public TicketService(@Qualifier("ticketJdbc")TicketDAO ticketDAO, UserClient userClient, MovieClient movieClient, CinemaClient cinemaClient, SeatClient seatClient, ScheduleClient scheduleClient) {
        this.ticketDAO = ticketDAO;
        this.userClient = userClient;
        this.movieClient = movieClient;
        this.cinemaClient = cinemaClient;
        this.seatClient = seatClient;
        this.scheduleClient = scheduleClient;
    }

    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketDAO.selectAllTickets();

        return tickets.stream().map(ticket -> {

            Long userId = userClient.getUserById(ticket.getUserId());
            Long movieId = movieClient.getMovieById(ticket.getMovieId());
            Long cinemaId = cinemaClient.getCinemaById(ticket.getCinemaId());
            Long seatId = seatClient.getSeatById(ticket.getSeatId());
            Long scheduleId = scheduleClient.getScheduleById(ticket.getScheduleId());
            return new TicketDTO(
                    ticket.getTicketId(),
                    userId,
                    movieId,
                    cinemaId,
                    seatId,
                    scheduleId,
                    ticket.getPrice(),
                    ticket.getDate());


        }).collect(Collectors.toList());
    }
}
