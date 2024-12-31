package com.movie.ticket.ticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */

@Repository("ticketJdbc")
@Slf4j
public class TicketAccessService implements TicketDAO  {
    private final JdbcTemplate jdbcTemplate;

    private final TicketRowMapper rowMapper;

    public TicketAccessService(JdbcTemplate jdbcTemplate, TicketRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

  @Override
    public List<Ticket> selectAllTickets() {
        var sql = """
                SELECT * FROM ticket
                """;
        return jdbcTemplate.query(sql,rowMapper);
    }

  @Override
    public void createOneTicket(Ticket ticket) {
      log.info("Creating ticket for ticketId: {} userId: {}, movieId: {}", ticket.getTicketId(), ticket.getUserId(), ticket.getMovieId());

      var sql =
              """
                INSERT INTO ticket (user_id,movie_id,cinema_id,seat_id,schedule_id,price,date)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcTemplate.update(connection -> {
          PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          ps.setLong(1, ticket.getUserId());
          ps.setLong(2, ticket.getMovieId());
          ps.setLong(3, ticket.getCinemaId());
          ps.setLong(4, ticket.getSeatId());
          ps.setLong(5, ticket.getScheduleId());
          ps.setBigDecimal(6, ticket.getPrice());
          ps.setDate(7, new java.sql.Date(ticket.getDate().getTime()));
          return ps;
      }, keyHolder);
      }

    @Override
    public void updateTicket(Ticket updatTicket) {

        if(updatTicket.getUserId()!=null){
            var sql = """
                    UPDATE ticket SET user_id=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getUserId(),
                    updatTicket.getTicketId());
        }
        if(updatTicket.getMovieId()!=null){
            var sql = """
                    UPDATE ticket SET movie_id=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getMovieId(),
                    updatTicket.getTicketId());
        }
        if(updatTicket.getCinemaId()!=null){
            var sql = """
                    UPDATE ticket SET cinema_id=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getCinemaId(),
                    updatTicket.getTicketId());
        }
        if(updatTicket.getSeatId()!=null){
            var sql = """
                    UPDATE ticket SET seat_id=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getSeatId(),
                    updatTicket.getTicketId());
        }
        if(updatTicket.getScheduleId()!=null){
            var sql = """
                    UPDATE ticket SET schedule_id=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getScheduleId(),
                    updatTicket.getTicketId());
        }
        if(updatTicket.getPrice()!=null){
            var sql = """
                    UPDATE ticket SET price=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getPrice(),
                    updatTicket.getTicketId());
        }

        if(updatTicket.getDate()!=null){
            var sql = """
                    UPDATE ticket SET date=? WHERE ticket_id =?
                    """;
            jdbcTemplate.update(sql,
                    updatTicket.getDate(),
                    updatTicket.getTicketId());
        }

    }

    @Override
    public Optional<Ticket> selectTicketById(Long ticketId) {
        var sql = """
                
                SELECT * FROM ticket WHERE ticket_id=?
                """;
        return jdbcTemplate.query(sql,rowMapper,ticketId)
                .stream()
                .findFirst();
    }

    @Override
    public void deleteTicket(Long ticketId) {
        var sql = """
                
                DELETE FROM ticket WHERE ticket_id=?
                """;
        jdbcTemplate.update(sql,ticketId);
    }
}
