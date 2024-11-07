package com.movie.seats.seat;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
                SELECT seat_id,seat_number,row,type,cinema_id
                FROM seats
                
                """;
        return jdbcTemplate.query(sql,seatRowMapper);
    }

    @Override
    public Optional<Seat> selectSeatById(Long seatId) {
        var sql= """
                SELECT seat_id,seat_number,row,type,cinema_id
                FROM seats where seat_id=?
                
                """;

        return jdbcTemplate.query(sql,seatRowMapper,seatId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertSeat(Seat seat) {
        var sql = """
        INSERT INTO seats (seat_number, row, type, cinema_id) VALUES (?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, seat.getSeatNumber(), seat.getRow(), seat.getType(), seat.getCinemaId());
    }

    @Override

    public List<Seat> selectSeatsByCinemaId(Long cinemaId) {
        var sql = """
                SELECT seat_id, seat_number, row, type,cinema_id
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
    public boolean ifSeatOccupied(String name) {
        return false;
    }

    @Override
    public void updateSeat(Seat updateSeat) {

    }
}
