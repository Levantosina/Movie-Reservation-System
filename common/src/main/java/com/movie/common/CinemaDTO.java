package com.movie.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author DMITRII LEVKIN on 25/12/2024
 * @project Movie-Reservation-System
 */
public record CinemaDTO(
        @JsonProperty("cinema_id") Long cinemaId,
        @JsonProperty("cinema_name") String cinemaName,
        @JsonProperty("cinema_location") String cinemaLocation
) { }