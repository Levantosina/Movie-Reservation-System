package com.movie.schedules.movieschedules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<MovieScheduleDTO>getAllMovie(){

        return movieScheduleService.getAllSchedules();
    }
    @PostMapping("/schedules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieSchedule> createSchedule(@RequestBody MovieScheduleRegistrationRequest request) {
        System.out.println("Received request: " + request);
        MovieSchedule movieSchedule = movieScheduleService.createSchedule(request);
        return ResponseEntity.ok(movieSchedule);
    }

    @PutMapping("{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
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
