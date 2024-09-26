package com.movie.resrvation.seat;

import com.movie.resrvation.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Service
public class SeatService {

    private final SeatDAO seatDAO;
    private final SeatDTOMapper seatDTOMapper;

    public SeatService(@Qualifier("seatJdbc")SeatDAO seatDAO, SeatDTOMapper seatDTOMapper) {
        this.seatDAO = seatDAO;
        this.seatDTOMapper = seatDTOMapper;
    }

    public List<SeatDTO> getAllSeats(){
        return seatDAO.selectAllSeats()
                .stream()
                .map(seatDTOMapper)
                .collect(Collectors.toList());
    }

    public SeatDTO getSeat(Long seatId){
        return seatDAO.selectSeatById(seatId)
                .map(seatDTOMapper)
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Seat with id [%s] not found".formatted(seatId)
                        )
                );
    }

    public void registerNewSeat( SeatRegistrationRequest seatRegistrationRequest){
        Seat seat= new Seat();
        seat.setSeatNumber(seatRegistrationRequest.seatNumber());
        seat.setRow(seatRegistrationRequest.row());
        seat.setType(seatRegistrationRequest.type());

        seatDAO.insertSeat(seat);
    }
}
