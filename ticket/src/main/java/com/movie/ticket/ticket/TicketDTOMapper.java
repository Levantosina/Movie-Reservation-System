package com.movie.ticket.ticket;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@Service
public class TicketDTOMapper  implements Function<Ticket,TicketDTO> {
    @Override
    public TicketDTO apply(Ticket ticket) {
        return new TicketDTO(
                ticket.getTicketId(),
                ticket.getUserId(),
                ticket.getMovieId(),
                ticket.getCinemaId(),
                ticket.getSeatId(),
                ticket.getScheduleId(),
                ticket.getPrice(),
                ticket.getDate()
        );
    }
}
