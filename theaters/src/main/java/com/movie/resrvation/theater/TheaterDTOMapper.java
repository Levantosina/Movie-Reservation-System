package com.movie.resrvation.theater;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */

@Service
public class TheaterDTOMapper implements Function<Theater,TheaterDTO> {
    @Override
    public TheaterDTO apply(Theater theater) {
        return new TheaterDTO(
                theater.getTheaterId(),
                theater.getTheaterName(),
                theater.getTheaterLocation()
        );
    }
}
