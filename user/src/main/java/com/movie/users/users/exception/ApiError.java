package com.movie.users.users.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
public record ApiError (
        String path,
        String message,
        int statusCode,
        LocalDateTime time
){
}
