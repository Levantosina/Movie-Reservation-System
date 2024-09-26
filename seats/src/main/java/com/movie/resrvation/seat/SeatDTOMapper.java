package com.movie.resrvation.seat;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Service
public class SeatDTOMapper implements Function<Seat,SeatDTO> {
    @Override
    public SeatDTO apply(Seat seat) {
        return new SeatDTO(
                seat.getSeatId(),
                seat.getSeatNumber(),
                seat.getRow(),
                seat.getType()
        );
    }
}
