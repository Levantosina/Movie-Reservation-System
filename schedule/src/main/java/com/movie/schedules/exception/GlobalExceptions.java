package com.movie.schedules.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author DMITRII LEVKIN on 19/12/2024
 * @project Movie-Reservation-System
 */
@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler(ScheduleRequestValidationException.class)
    public ResponseEntity<String> handleScheduleRequestValidationException(ScheduleRequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

