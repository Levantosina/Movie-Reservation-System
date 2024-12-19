package com.movie.seats.seat;


import com.movie.seats.SeatAbstractDaoUnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */

@Slf4j
class SeatAccessServiceTest extends SeatAbstractDaoUnitTest {

    private SeatAccessService underTest;


    @BeforeEach
    void setUp() {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM seats");
        underTest = new SeatAccessService(jdbcTemplate, getJdbcTemplate().getDataSource(), new SeatRowMapper());
    }

    @Test
    void selectAllSeats() {

        Seat seat = Seat.builder()
                .seatNumber(3)
                .row("A")
                .type("VIP")
                .cinemaId(21L)
                .isOccupied(true)
                .build();
        underTest.insertSeat(seat);
        List<Seat> actual = underTest.selectAllSeats();
        assertThat(actual).isNotEmpty();
    }




    @Test
    void selectSeatById() {

        Seat seat = Seat.builder()
                .seatNumber(3)
                .row("A")
                .type("VIP")
                .cinemaId(1L)
                .isOccupied(false)
                .build();
        underTest.insertSeat(seat);


        long seatId = underTest.selectAllSeats()
                .stream()
                .map(Seat::getSeatId)
                .findFirst()
                .orElseThrow();


        Optional<Seat> actual = underTest.selectSeatById(seatId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getSeatId()).isEqualTo(seatId);
            assertThat(c.getSeatNumber()).isEqualTo(3);
            assertThat(c.getRow()).isEqualTo("A");
            assertThat(c.getType()).isEqualTo("VIP");
            assertThat(c.getCinemaId()).isEqualTo(1L);
            assertThat(c.isOccupied()).isEqualTo(false);
        });
    }

    @Test
    void countSeatsByCinemaId() {
        long cinemaId=25;
        Seat seat = Seat.builder()
                .seatNumber(1)
                .row("A")
                .type("VIP")
                .cinemaId(cinemaId)
                .isOccupied(false)
                .build();
        underTest.insertSeat(seat);
        int seatCount = underTest.countSeatsByCinemaId(cinemaId);
        assertThat(seatCount).isEqualTo(1);
    }

    @Test
    void ifSeatOccupied() {
    }

    @Test
    void updateSeat() {
    }
}