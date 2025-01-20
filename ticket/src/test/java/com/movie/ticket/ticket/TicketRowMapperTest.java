package com.movie.ticket.ticket;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TicketRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        TicketRowMapper ticketRowMapper = new TicketRowMapper();
        ResultSet resultSet =  mock(ResultSet.class);

        when(resultSet.getLong("ticket_id")).thenReturn(1L);
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getLong("movie_id")).thenReturn(1L);
        when(resultSet.getLong("cinema_id")).thenReturn(1L);
        when(resultSet.getLong("seat_id")).thenReturn(1L);
        when(resultSet.getLong("schedule_id")).thenReturn(1L);
        when(resultSet.getBigDecimal("price")).thenReturn(BigDecimal.valueOf(19));
        when(resultSet.getDate("date")).thenReturn(Date.valueOf("2024-01-01"));

        Ticket ticket = ticketRowMapper.mapRow(resultSet, 1);
        Ticket expected = new Ticket(1L,1L,1L,1L,1L,1L,new BigDecimal(19),Date.valueOf("2024-01-01"));
        assertThat(ticket).isEqualTo(expected);
    }
}


