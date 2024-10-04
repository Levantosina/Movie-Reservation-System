package com.movie.cinema.cinema;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */

@Service
public class CinemaDTOMapper implements Function<Cinema, CinemaDTO> {
    @Override
    public CinemaDTO apply(Cinema cinema) {
        return new CinemaDTO(
                cinema.getCinemaId(),
                cinema.getCinemaName(),
                cinema.getCinemaLocation()
        );
    }
}
