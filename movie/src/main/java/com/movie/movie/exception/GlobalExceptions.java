package com.movie.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author DMITRII LEVKIN on 09/12/2024
 * @project Movie-Reservation-System
 */
@ControllerAdvice
public class GlobalExceptions {
        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
}

