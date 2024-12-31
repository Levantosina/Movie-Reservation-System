package com.movie.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
@JsonDeserialize(using = TotalSeatsDTODeserializer.class)
public record TotalSeatsDTO(Integer totalSeats) {
    public Integer getTotalSeats() {
        return totalSeats;
    }
}