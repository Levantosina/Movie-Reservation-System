package com.movie.resrvation.seat;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
public record SeatRegistrationRequest(
        String seatNumber,
        String row,
        String type,
        Long cinemaId
) {
}
