package com.movie.ticket.ticket;

import com.movie.ticket.TicketAbstractDaoUnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TicketAccessServiceTest  extends TicketAbstractDaoUnitTest {
    private TicketAccessService underTest;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM ticket");
        underTest = new TicketAccessService(jdbcTemplate, getJdbcTemplate().getDataSource(),new TicketRowMapper());

    }

    @Test
    void selectAllTickets() {

        Ticket ticket = Ticket.builder()
                .userId(1L)
                .movieId(1L)
                .cinemaId(1L)
                .seatId(1L)
                .scheduleId(1L)
                .price(BigDecimal.valueOf(32.00))
                .date(Date.valueOf("2025-12-12"))
                .build();
        underTest.createOneTicket(ticket);
        List<Ticket> tickets = underTest.selectAllTickets();
        assertThat(tickets).isNotEmpty();
    }

    @Test
    void createOneTicket() {
        Ticket ticket = Ticket.builder()
                .userId(1L)
                .movieId(1L)
                .cinemaId(1L)
                .seatId(1L)
                .scheduleId(1L)
                .price(BigDecimal.valueOf(32.00))
                .date(Date.valueOf("2025-12-12"))
                .build();
        underTest.createOneTicket(ticket);
    }

    @Test
    void updateTicket() {
        Ticket ticket = Ticket.builder()
                .userId(1L)
                .movieId(1L)
                .cinemaId(1L)
                .seatId(1L)
                .scheduleId(1L)
                .price(BigDecimal.valueOf(32.00))
                .date(Date.valueOf("2025-12-12"))
                .build();
        underTest.createOneTicket(ticket);

        long ticketId= underTest.selectAllTickets()
                .stream()
                .map(Ticket::getTicketId)
                .findFirst()
                .orElseThrow();
        Long updateMovieId = 2L;
        Long updateCinemaId = 2L;
        Date updateDate = Date.valueOf("2025-12-13");

        ticket.setMovieId(updateMovieId);
        ticket.setCinemaId(updateCinemaId);
        ticket.setDate(updateDate);

        underTest.updateTicket(ticket);

        Optional<Ticket> updateTicket= underTest.selectTicketById(ticketId);
        assertThat(updateTicket).isPresent().hasValueSatisfying(
                t->{
                    assertThat(t.getMovieId()).isEqualTo(updateMovieId);
                    assertThat(t.getCinemaId()).isEqualTo(updateCinemaId);
                    assertThat(t.getDate()).isEqualTo(updateDate);
                }
        );
    }

    @Test
    void selectTicketById() {
        Ticket ticket = Ticket.builder()
                .userId(1L)
                .movieId(1L)
                .cinemaId(1L)
                .seatId(1L)
                .scheduleId(1L)
                .price(BigDecimal.valueOf(32.00).setScale(2))
                .date(Date.valueOf("2025-12-12"))
                .build();
        underTest.createOneTicket(ticket);

        long ticketId= underTest.selectAllTickets()
                .stream()
                .map(Ticket::getTicketId)
                .findFirst()
                .orElseThrow();




        Optional<Ticket> updateTicket= underTest.selectTicketById(ticketId);

        assertThat(updateTicket).isPresent().hasValueSatisfying(
                t->{
                    assertThat(t.getTicketId()).isEqualTo(ticketId);
                    assertThat(t.getUserId()).isEqualTo(ticket.getUserId());
                    assertThat(t.getMovieId()).isEqualTo(ticket.getMovieId());
                    assertThat(t.getCinemaId()).isEqualTo(ticket.getCinemaId());
                    assertThat(t.getSeatId()).isEqualTo(ticket.getSeatId());
                    assertThat(t.getScheduleId()).isEqualTo(ticket.getScheduleId());
                    assertThat(t.getPrice()).isEqualTo(ticket.getPrice());
                    assertThat(t.getDate()).isEqualTo(ticket.getDate());


                }
        );
    }

    @Test
    void deleteTicket() {
        Ticket ticket = Ticket.builder()
                .userId(1L)
                .movieId(1L)
                .cinemaId(1L)
                .seatId(1L)
                .scheduleId(1L)
                .price(BigDecimal.valueOf(32.00).setScale(2))
                .date(Date.valueOf("2025-12-12"))
                .build();
        underTest.createOneTicket(ticket);

        long ticketId= underTest.selectAllTickets()
                .stream()
                .map(Ticket::getTicketId)
                .findFirst()
                .orElseThrow();

        underTest.deleteTicket(ticketId);

        Optional<Ticket> deleteTicket= underTest.selectTicketById(ticketId);
        assertThat(deleteTicket).isNotPresent();
    }
}