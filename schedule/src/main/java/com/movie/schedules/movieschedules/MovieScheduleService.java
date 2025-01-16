package com.movie.schedules.movieschedules;

import com.movie.client.cinemaClient.CinemaClient;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.common.TotalSeatsDTO;
import com.movie.exceptions.AlreadyOccupiedException;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private final MovieClient movieClient;
    private final CinemaClient cinemaClient;


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

        if (!cinemaClient.existsById(movieScheduleRegistrationRequest.cinemaId())) {
            throw new ResourceNotFoundException("Cinema with ID " + movieScheduleRegistrationRequest.cinemaId() + " does not exist.");
        }


        if (!movieClient.existsById(movieScheduleRegistrationRequest.movieId())) {
            throw new ResourceNotFoundException("Movie with ID " + movieScheduleRegistrationRequest.movieId() + " does not exist.");
        }

        if (movieScheduleRegistrationRequest.startTime().isBefore(LocalTime.of(10, 0)) ||
                movieScheduleRegistrationRequest.endTime().isAfter(LocalTime.of(22, 0))) {
            throw new RuntimeException("Schedule time must be between 10:00 AM and 10:00 PM.");
        }

        TotalSeatsDTO totalSeatsDTO = seatClient.getTotalSeatsByCinemaId(movieScheduleRegistrationRequest.cinemaId());

        if (totalSeatsDTO == null || totalSeatsDTO.getTotalSeats() == null) {
            throw new RuntimeException("Unable to fetch seat data for the specified cinema.");
        }

        LocalDate scheduleDate = movieScheduleRegistrationRequest.date();
        Long movieId = movieScheduleRegistrationRequest.movieId();

        long existingSchedulesCount = movieScheduleDAO.countSchedulesForMovieOnDate(movieId, scheduleDate);

        if (existingSchedulesCount >= 2) {
            throw new AlreadyOccupiedException("A maximum of 2 showings per day for the same movie is allowed.");
        }

        List<MovieSchedule> existingSchedules = movieScheduleDAO.getSchedulesForCinemaOnDate(
                movieScheduleRegistrationRequest.cinemaId(),
                scheduleDate
        );

        for (MovieSchedule existingSchedule : existingSchedules) {
            if (isOverlapping(
                    movieScheduleRegistrationRequest.startTime(),
                    movieScheduleRegistrationRequest.endTime(),
                    existingSchedule.getStartTime(),
                    existingSchedule.getEndTime())) {
                throw new RuntimeException("Schedule overlaps with an existing schedule for this cinema.");
            }
        }


        MovieSchedule movieSchedule = new MovieSchedule();
        movieSchedule.setDate(scheduleDate);
        movieSchedule.setStartTime(movieScheduleRegistrationRequest.startTime());
        movieSchedule.setEndTime(movieScheduleRegistrationRequest.endTime());
        movieSchedule.setAvailableSeats(totalSeatsDTO.totalSeats());
        movieSchedule.setCinemaId(movieScheduleRegistrationRequest.cinemaId());
        movieSchedule.setMovieId(movieScheduleRegistrationRequest.movieId());
        movieScheduleDAO.createSchedule(movieSchedule);
    }
    private boolean isOverlapping(LocalTime newStart, LocalTime newEnd, LocalTime existingStart, LocalTime existingEnd) {
        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
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
    public String findStartTimeByScheduleId(Long scheduleId) {
        return movieScheduleDAO.selectStartTimeByScheduleId(scheduleId)
                .map(LocalTime::toString)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Start time with id [%s] not found".formatted(scheduleId)
                ));
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

