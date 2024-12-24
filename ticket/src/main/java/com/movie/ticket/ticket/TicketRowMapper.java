package com.movie.ticket.ticket;

import jakarta.persistence.Column;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@Component
public class TicketRowMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Ticket.builder()
                .seatId(rs.getLong("ticket_id"))
                .userId(rs.getLong("user_id"))
                .movieId(rs.getLong("movie_id"))
                .cinemaId(rs.getLong("cinema_id"))
                .seatId(rs.getLong("seat_id"))
                .scheduleId(rs.getLong("schedule_id"))
                .price(rs.getBigDecimal("price"))
                .date(rs.getDate("date"))
                .build();
    }
}
