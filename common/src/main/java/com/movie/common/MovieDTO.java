package com.movie.common;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
public record MovieDTO(


        Long movieId,
        String movieName,
        Integer year,
        String country,
        String genre,
        String description

) {}