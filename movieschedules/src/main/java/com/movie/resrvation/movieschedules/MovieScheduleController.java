package com.movie.resrvation.movieschedules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/schedules")
public class MovieScheduleController {
    private final MovieScheduleService movieScheduleService;

    public MovieScheduleController(MovieScheduleService movieScheduleService) {
        this.movieScheduleService = movieScheduleService;
    }

    @PostMapping
    public void createSchedule(@RequestBody MovieScheduleRegistrationRequest movieScheduleRegistrationRequest) {
        log.info("New schedule registration: {}", movieScheduleRegistrationRequest);
        movieScheduleService.createSchedule(movieScheduleRegistrationRequest);
    }

    @PutMapping("{scheduleId}")
    public void updateSchedule(@PathVariable Long scheduleId, @RequestBody MovieScheduleRegistrationRequest movieScheduleRegistrationRequest) {
        movieScheduleService.updateSchedule(scheduleId, movieScheduleRegistrationRequest);
    }

    @GetMapping("/cinema/{cinemaId}")
    public List<MovieScheduleDTO> getScheduleByCinemaId(@PathVariable("cinemaId") Long cinemaId) {
        return movieScheduleService.findByCinemaId(cinemaId);
    }

    @GetMapping("/movie/{movieId}")
    public List<MovieScheduleDTO> getScheduleByMovieId(@PathVariable("movieId") Long movieId) {
        return movieScheduleService.findByMovieId(movieId);
    }

    @GetMapping("/date/{date}")
    public List<MovieScheduleDTO> getScheduleByDate(@PathVariable("date") LocalDate date) {
        return movieScheduleService.findByDate(date);
    }

    @GetMapping("/cinema/{cinemaId}/movie/{movieId}")
    public List<MovieScheduleDTO> getScheduleByDate(@PathVariable("cinemaId") Long cinemaId, @PathVariable("movieId") Long movieId) {
        return movieScheduleService.findByCinemaIdAndMovieId(cinemaId, movieId);
    }

    @DeleteMapping("{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        movieScheduleService.deleteSchedule(scheduleId);

    }
}
