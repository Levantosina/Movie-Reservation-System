package com.movie.ticket.ticket;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
public interface TicketDAO {

    List<Ticket> selectAllTickets();

    void createOneTicket(Ticket ticket);

    void updateTicket(Ticket ticket);

    Optional<Ticket> selectTicketById(Long ticketId);

    boolean existTicketWithId(Long id);

    void deleteTicket(Long ticketId);
}
