package com.movie.resrvation.cinema;

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
public class CinemaService {

    private  final CinemaDAO cinemaDAO;
    private final CinemaDTOMapper cinemaDTOMapper;

    public CinemaService(@Qualifier("cinemaJdbc") CinemaDAO cinemaDAO, CinemaDTOMapper cinemaDTOMapper) {
        this.cinemaDAO = cinemaDAO;
        this.cinemaDTOMapper = cinemaDTOMapper;
    }
    public List<CinemaDTO> getAllCinemas() {
        return cinemaDAO.selectAllCinemas()
                .stream()
                .map(cinemaDTOMapper)
                .collect(Collectors.toList());
    }
    public CinemaDTO getCinema(Long id) {
        return cinemaDAO.selectCinemaById(id)
                .map(cinemaDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(id)));
    }

    public void registerCinema(CinemaRegistrationRequest cinemaRegistrationRequest){

        Cinema cinema = new Cinema();
        cinema.setCinemaName(cinemaRegistrationRequest.cinemaName());
        cinema.setCinemaLocation(cinemaRegistrationRequest.cinemaLocation());

        cinemaDAO.insertCinema(cinema);

    }

}
