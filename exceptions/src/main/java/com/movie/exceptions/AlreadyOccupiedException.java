package com.movie.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 30/12/2024
 * @project Movie-Reservation-System
 */

@ResponseStatus(code= HttpStatus.CONFLICT)
public class AlreadyOccupiedException extends RuntimeException {
    public AlreadyOccupiedException(String message) {

        super(message);
    }
}
