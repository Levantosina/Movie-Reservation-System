package com.movie.seats.seat;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
public interface SeatDAO {
    List<Seat> selectAllSeats();
    Optional<Seat> selectSeatById(Long seatId);

    void insertSeat(Seat seat);



    boolean ifSeatOccupied(String name);

    void  updateSeat(Seat updateSeat);

    List<Seat> selectSeatsByCinemaId(Long cinemaId);

    int countSeatsByCinemaId(Long cinemaId);
}
