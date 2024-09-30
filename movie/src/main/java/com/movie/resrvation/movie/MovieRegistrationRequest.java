package com.movie.resrvation.movie;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
public record MovieRegistrationRequest (
        String movieName,
        Long year,
        String country,
        String genre,
        String description
){
}
