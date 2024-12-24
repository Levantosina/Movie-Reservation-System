package com.movie.ticket.ticket;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
public record TicketDTO(
        Long ticketId,
        Long userId,
        Long movieId,
        Long cinemaId,
        Long seatId,
        Long scheduleId,
        BigDecimal price,
        Date date

) { }
