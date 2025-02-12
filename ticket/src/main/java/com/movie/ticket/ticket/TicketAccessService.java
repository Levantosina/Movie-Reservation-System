package com.movie.ticket.ticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */

@Repository("ticketJdbc")
@Slf4j
public class TicketAccessService implements TicketDAO  {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final TicketRowMapper rowMapper;

    public TicketAccessService(JdbcTemplate jdbcTemplate, DataSource dataSource,TicketRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
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
    public List<Ticket> selectTicketsByUser(Long user) {
        var sql = """
                SELECT * FROM ticket WHERE user_id=?;
                """;
        return jdbcTemplate.query(sql,rowMapper,user);
    }

    @Override
    public void createOneTicket(Ticket ticket) {
        var sql = """
          INSERT INTO ticket (user_id, movie_id, cinema_id, seat_id, schedule_id, price, date)
          VALUES (:userId, :movieId, :cinemaId, :seatId, :scheduleId, :price, :date)
          """;
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", ticket.getUserId())
                .addValue("movieId", ticket.getMovieId())
                .addValue("cinemaId", ticket.getCinemaId())
                .addValue("seatId", ticket.getSeatId())
                .addValue("scheduleId", ticket.getScheduleId())
                .addValue("price", ticket.getPrice())
                .addValue("date", new java.sql.Date(ticket.getDate().getTime()));

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, keyHolder);


        Map<String, Object> keys = keyHolder.getKeys();

        if (keys != null && keys.containsKey("ticket_id")) {
            ticket.setTicketId(((Number) keys.get("ticket_id")).longValue());
        } else {
            log.error("Failed to retrieve generated key for ticket_id");
            throw new IllegalStateException("Failed to retrieve generated key for ticket_id");
        }
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
    public boolean existTicketWithId(Long ticketId) {
        var sql= """
                SELECT count(ticket_id)FROM users
                where ticket_id=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,ticketId);
        return count!=null && count>0;
    }

    @Override
    public void deleteTicket(Long ticketId) {
        var sql = """
                
                DELETE FROM ticket WHERE ticket_id=?
                """;
        jdbcTemplate.update(sql,ticketId);
    }
}
