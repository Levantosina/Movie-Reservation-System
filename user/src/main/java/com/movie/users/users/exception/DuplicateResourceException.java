package com.movie.users.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 14/10/2024
 * @project MovieReservationSystem
 */
@ResponseStatus(code= HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
