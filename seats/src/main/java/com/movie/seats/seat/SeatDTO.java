package com.movie.seats.seat;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
public record SeatDTO(
        Long seatId,
        String seatNumber,
        String row,
        String type

) { }
