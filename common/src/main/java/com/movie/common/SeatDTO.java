package com.movie.common;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
public record SeatDTO(
        Long seatId,
        Integer seatNumber,
        String row,
        String type,
        Boolean isOccupied



) { }