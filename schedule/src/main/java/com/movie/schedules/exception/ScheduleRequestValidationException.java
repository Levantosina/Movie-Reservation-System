package com.movie.schedules.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DMITRII LEVKIN on 19/12/2024
 * @project Movie-Reservation-System
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ScheduleRequestValidationException extends  RuntimeException {
    public ScheduleRequestValidationException (String m){
        super(m);
    }
}



