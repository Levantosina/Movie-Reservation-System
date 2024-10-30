package com.movie.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 14/10/2024
 * @project MovieReservationSystem
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MovieRequestValidationException extends RuntimeException {
    public MovieRequestValidationException(String message) {
        super(message);
    }
}
