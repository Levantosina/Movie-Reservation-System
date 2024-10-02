package com.movie.resrvation.movieschedules;

import com.movie.resrvation.exception.ResourceNotFoundException;
import com.movie.resrvation.seat.SeatService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Service
public class MovieScheduleService {
    private final MovieScheduleDAO movieScheduleDAO;

    private  final MovieScheduleDTOMapper movieScheduleDTOMapper;

    private final SeatService seatService;


    public MovieScheduleService(@Qualifier("scheduleJdbc") MovieScheduleDAO movieScheduleDAO, MovieScheduleDTOMapper movieScheduleDTOMapper, SeatService seatService) {
        this.movieScheduleDAO = movieScheduleDAO;


        this.movieScheduleDTOMapper = movieScheduleDTOMapper;
        this.seatService = seatService;
    }



    public void createSchedule(MovieScheduleRegistrationRequest movieScheduleRegistrationRequest) {

        int totalSeats = seatService.getTotalSeatsByCinemaId(movieScheduleRegistrationRequest.cinemaId());
        MovieSchedule movieSchedule = new MovieSchedule();
        movieSchedule.setMovieId(movieScheduleRegistrationRequest.movieId());
        movieSchedule.setCinemaId(movieScheduleRegistrationRequest.cinemaId());
        movieSchedule.setStartTime(movieScheduleRegistrationRequest.startTime());
        movieSchedule.setEndTime(movieScheduleRegistrationRequest.endTime());
        movieSchedule.setDate(movieScheduleRegistrationRequest.date());
        movieSchedule.setAvailableSeats(totalSeats); // Set available seats

        movieScheduleDAO.insertSchedule(movieSchedule);
    }

    public void updateSchedule(Long scheduleId, MovieScheduleRegistrationRequest movieScheduleRegistrationRequest) {
        MovieSchedule existingSchedule = movieScheduleDAO.selectScheduleById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Schedule with id [%s] not found".formatted(scheduleId))
                );

        existingSchedule.setMovieId(movieScheduleRegistrationRequest.movieId());
        existingSchedule.setCinemaId(movieScheduleRegistrationRequest.cinemaId());
        existingSchedule.setStartTime(movieScheduleRegistrationRequest.startTime());
        existingSchedule.setEndTime(movieScheduleRegistrationRequest.endTime());
        existingSchedule.setDate(movieScheduleRegistrationRequest.date());

        movieScheduleDAO.upDateSchedule(existingSchedule);
    }
    public List<MovieScheduleDTO> findByCinemaId(Long cinemaId) {
        return movieScheduleDAO.selectSchedulesByCinemaId(cinemaId)
                .stream()
                .map(movieScheduleDTOMapper)
                .collect(Collectors.toList());
    }

    public List<MovieScheduleDTO> findByMovieId(Long movieId) {
        return movieScheduleDAO.selectSchedulesByMovieId(movieId)
                .stream()
                .map(movieScheduleDTOMapper)
                .collect(Collectors.toList());
    }

    public List<MovieScheduleDTO> findByDate(LocalDate date) {
        return movieScheduleDAO.findByDate(date)
                .stream()
                .map(movieScheduleDTOMapper)
                .collect(Collectors.toList());
    }
    public List<MovieScheduleDTO> findByCinemaIdAndMovieId(Long cinemaId, Long movieId) {
        return movieScheduleDAO.findByCinemaIdAndMovieId(cinemaId, movieId)
                .stream()
                .map(movieScheduleDTOMapper)
                .collect(Collectors.toList());
    }

    public void deleteSchedule(Long scheduleId){
        movieScheduleDAO.deleteSchedule(scheduleId);
    }
}