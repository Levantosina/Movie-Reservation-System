package com.movie.schedules.movieschedules;


import com.movie.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public ResponseEntity<?>getAllMovie(){
       List<MovieScheduleDTO> scheduleDTO = movieScheduleService.getAllSchedules();
        return ResponseEntity.ok(scheduleDTO);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<MovieScheduleDTO> getSchedule(@PathVariable("scheduleId") Long scheduleId) {
        MovieScheduleDTO movieScheduleDTO = movieScheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(movieScheduleDTO);
    }
    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody MovieScheduleRegistrationRequest request, BindingResult bindingResult) {
        List<String> errorMessage= new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }

        movieScheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest updateRequest,
            BindingResult bindingResult) {

        List<String> errorMessage= new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        movieScheduleService.updateSchedule(scheduleId, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
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

    @PutMapping("/{scheduleId}/decrease")
    public ResponseEntity<?> decreaseAvailableSeats(@PathVariable Long scheduleId) {
        try {
            movieScheduleService.decreaseAvailableSeats(scheduleId);
            return ResponseEntity.ok("Available seats decreased successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        movieScheduleService.deleteSchedule(scheduleId);

    }
}
