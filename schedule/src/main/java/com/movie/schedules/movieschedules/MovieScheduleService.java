package com.movie.schedules.movieschedules;

import com.movie.client.seatClient.SeatClient;
import com.movie.common.TotalSeatsDTO;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
    @Service
    @AllArgsConstructor
    public class MovieScheduleService {
        private final MovieScheduleDAO movieScheduleDAO;
        private final MovieScheduleDTOMapper movieScheduleDTOMapper;
        private final SeatClient seatClient;


    public List<MovieScheduleDTO>getAllSchedules(){
        return movieScheduleDAO.selectAllSchedules()
                .stream()
                .map(movieScheduleDTOMapper)
                .collect(Collectors.toList());
    }

    public MovieScheduleDTO getScheduleById(Long scheduleId) {
        return movieScheduleDAO.selectScheduleById(scheduleId)
                .map(movieScheduleDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Schedule with id [%s] not found".formatted(scheduleId))
                );
    }

    public void createSchedule(MovieScheduleRegistrationRequest movieScheduleRegistrationRequest) {

        TotalSeatsDTO totalSeatsDTO = seatClient.getTotalSeatsByCinemaId(movieScheduleRegistrationRequest.cinemaId());

        if (totalSeatsDTO == null || totalSeatsDTO.getTotalSeats() == null) {
            throw new RuntimeException("Unable to fetch seat data for the specified cinema.");
        }
        ///ADD EXEPTION!!!!!!!!!!!!!!!!!!!!

        MovieSchedule movieSchedule = new MovieSchedule();

        movieSchedule.setDate(movieScheduleRegistrationRequest.date());
        movieSchedule.setStartTime(movieScheduleRegistrationRequest.startTime());
        movieSchedule.setEndTime(movieScheduleRegistrationRequest.endTime());
        movieSchedule.setAvailableSeats(totalSeatsDTO.totalSeats());
        movieSchedule.setCinemaId(movieScheduleRegistrationRequest.cinemaId());
        movieSchedule.setMovieId(movieScheduleRegistrationRequest.movieId());


        movieScheduleDAO.createSchedule(movieSchedule);
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

        public void updateSchedule(Long scheduleId, ScheduleUpdateRequest updateRequest) {
            MovieSchedule schedule = movieScheduleDAO.selectScheduleById(scheduleId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Schedule with id [%s] not found".formatted(scheduleId))
                    );

            boolean changes = false;

            System.out.println("Old schedule: " + schedule);
            System.out.println("Update request: " + updateRequest);

            if (updateRequest.date() != null && !updateRequest.date().equals(schedule.getDate())) {
                schedule.setDate(updateRequest.date());
                changes = true;
            }
            if (updateRequest.startTime() != null && !updateRequest.startTime().equals(schedule.getStartTime())) {
                schedule.setStartTime(updateRequest.startTime());
                changes = true;
            }
            if (updateRequest.endTime() != null && !updateRequest.endTime().equals(schedule.getEndTime())) {
                schedule.setEndTime(updateRequest.endTime());
                changes = true;
            }
            if (updateRequest.availableSeats() != null &&
                    (schedule.getAvailableSeats() == null ||
                            !updateRequest.availableSeats().equals(schedule.getAvailableSeats()))) {
                System.out.println("Updating availableSeats from " + schedule.getAvailableSeats() +
                        " to " + updateRequest.availableSeats());
                schedule.setAvailableSeats(updateRequest.availableSeats());
                changes = true;
            } else if (updateRequest.availableSeats() == null) {
                System.out.println("No availableSeats update provided in request.");
            }


            if (updateRequest.cinemaId() != null && !updateRequest.cinemaId().equals(schedule.getCinemaId())) {
                schedule.setCinemaId(updateRequest.cinemaId());
                changes = true;
            }
            if (updateRequest.movieId() != null && !updateRequest.movieId().equals(schedule.getMovieId())) {
                schedule.setMovieId(updateRequest.movieId());
                changes = true;
            }

            if (!changes) {
                throw new RequestValidationException("No changes detected");
            }

            movieScheduleDAO.upDateSchedule(schedule);
        }


    public void decreaseAvailableSeats(Long scheduleId) {
        MovieSchedule schedule = movieScheduleDAO.selectScheduleById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        if (schedule.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No available seats for this schedule");
        }
        int updatedSeats = schedule.getAvailableSeats() - 1;
        movieScheduleDAO.updateAvailableSeats(scheduleId,updatedSeats);
    }

        public void deleteSchedule(Long scheduleId) {
            movieScheduleDAO.deleteSchedule(scheduleId);
        }
    }

