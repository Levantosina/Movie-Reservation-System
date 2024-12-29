package com.movie.exceptions;

/**
 * @author DMITRII LEVKIN on 17/11/2024
 * @project Movie-Reservation-System
 */
public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
