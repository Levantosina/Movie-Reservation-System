package com.movie.seats.seat;

import com.movie.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Repository("seatJdbc")
@Slf4j
public class SeatAccessService implements SeatDAO{

    private  final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;
    private final SeatRowMapper seatRowMapper;

    public SeatAccessService(JdbcTemplate jdbcTemplate, DataSource dataSource, SeatRowMapper seatRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.seatRowMapper = seatRowMapper;
    }


    @Override
    public List<Seat> selectAllSeats() {

        var sql= """
                SELECT seat_id,seat_number,row,type,cinema_id,is_occupied
                FROM seats
                
                """;
        return jdbcTemplate.query(sql,seatRowMapper);
    }



    @Override
    public Optional<Seat> selectSeatById(Long seatId) {
        var sql= """
                SELECT * FROM seats WHERE seat_id = ?
                
                """;
        try {
            Seat seat = jdbcTemplate.queryForObject(sql, seatRowMapper, seatId);
            return Optional.ofNullable(seat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void insertSeat(Seat seat) {
        var sql = """
        INSERT INTO seats (seat_number, row, type, cinema_id,is_occupied) VALUES (?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, seat.getSeatNumber(), seat.getRow(), seat.getType(), seat.getCinemaId(), seat.isOccupied());

    }



    @Override

    public List<Seat> selectSeatsByCinemaId(Long cinemaId) {
        var sql = """
                SELECT seat_id, seat_number, row, type,cinema_id,is_occupied
                FROM seats
                WHERE cinema_id = ?
                """;
        return jdbcTemplate.query(sql, seatRowMapper, cinemaId);
    }

    @Override
    public int countSeatsByCinemaId(Long cinemaId) {
        var sql = """
                SELECT COUNT(*) FROM seats WHERE cinema_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, cinemaId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;

    }




    @Override
    public boolean isSeatOccupied(Long seatId) {
        var sql = """
            SELECT is_occupied FROM seats WHERE seat_id = ?
            """;

        Boolean isOccupied = jdbcTemplate.queryForObject(sql, Boolean.class, seatId);
        if (isOccupied == null) {
            throw new ResourceNotFoundException("Seat with id [" + seatId + "] not found");
        }
            return isOccupied;

        }
    @Override
    public void updateSeatOccupation(Long seatId, boolean isOccupied) {
        var sql = """
             UPDATE seats SET is_occupied = ?  WHERE seat_id = ?
              """;
        jdbcTemplate.update(sql, isOccupied, seatId);
    }

    @Override
    public void updateSeat(Seat updateSeat) {
        if(updateSeat.getSeatNumber()!=null){
            var sql = """
                    UPDATE seats SET seat_number=? where seat_id=?
                    """;
        jdbcTemplate.update(sql,updateSeat.getSeatNumber(),updateSeat.getSeatId());
        }

        if(updateSeat.getRow()!=null){
            var sql = """
                    UPDATE seats SET row=? where seat_id=?
                    """;
            jdbcTemplate.update(sql,updateSeat.getRow(),updateSeat.getSeatId());
        }

        if(updateSeat.getType()!=null){
            var sql = """
                    UPDATE seats SET type=? where seat_id=?
                    """;
            jdbcTemplate.update(sql,updateSeat.getType(),updateSeat.getSeatId());
        }

        if(updateSeat.getCinemaId()!=null){
            var sql = """
                    UPDATE seats SET cinema_id=? where seat_id=?
                    """;
            jdbcTemplate.update(sql,updateSeat.getCinemaId(),updateSeat.getSeatId());
        }

        // occupied is boolean and don t need to check for null
            var sql = """
                UPDATE seats SET is_occupied=? WHERE seat_id=?
                """;
            jdbcTemplate.update(sql, updateSeat.isOccupied(), updateSeat.getSeatId());
        }


}
