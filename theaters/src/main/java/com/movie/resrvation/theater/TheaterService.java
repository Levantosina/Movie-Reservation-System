package com.movie.resrvation.theater;

import com.movie.resrvation.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Service
public class TheaterService {

    private  final TheaterDAO theaterDAO;
    private final TheaterDTOMapper theaterDTOMapper;

    public TheaterService(@Qualifier("theaterJdbc") TheaterDAO theaterDAO, TheaterDTOMapper theaterDTOMapper) {
        this.theaterDAO = theaterDAO;
        this.theaterDTOMapper = theaterDTOMapper;
    }
    public List<TheaterDTO> getAllTheaters() {
        return theaterDAO.selectAllTheaters()
                .stream()
                .map(theaterDTOMapper)
                .collect(Collectors.toList());
    }
    public TheaterDTO getTheater(Long id) {
        return theaterDAO.selectTheaterById(id)
                .map(theaterDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(id)));
    }

    public void registerTheater(TheaterRegistrationRequest theaterRegistrationRequest){

        Theater theater = new Theater();
        theater.setTheaterName(theaterRegistrationRequest.theaterName());
        theater.setTheaterLocation(theaterRegistrationRequest.theaterLocation());

        theaterDAO.insertTheater(theater);

    }

}
