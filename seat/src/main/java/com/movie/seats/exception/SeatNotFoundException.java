package com.movie.seats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String message) {
        super(message);
    }
}