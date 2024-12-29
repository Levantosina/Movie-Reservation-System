package com.movie.common;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
public record TotalSeatsDTO (
        int totalSeats

){

    public int getTotalSeats() {
        return totalSeats;
    }

}
