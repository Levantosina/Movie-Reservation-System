package com.movie.movie.movie;

/**
 * @author DMITRII LEVKIN on 24/10/2024
 * @project MovieReservationSystem
 */
public record MovieUpdateRequest(

         String movieName,
         Integer year,
         String country,
         String genre,
         String description

) {
}
