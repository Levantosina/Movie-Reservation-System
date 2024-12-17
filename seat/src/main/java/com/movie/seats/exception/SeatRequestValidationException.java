package com.movie.seats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 17/12/2024
 * @project Movie-Reservation-System
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SeatRequestValidationException extends RuntimeException {
    public SeatRequestValidationException(String message) {
        super(message);
    }
}