package com.movie.cinema.cinema;

import com.movie.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;

    }

    @GetMapping
    public ResponseEntity<?> getAllCinemas() {
        return ResponseEntity.ok( cinemaService.getAllCinemas());
    }

    @GetMapping("/{cinemaId}")
    public ResponseEntity<?> getCinemaById(@PathVariable("cinemaId") Long cinemaId) {

        return ResponseEntity.ok( cinemaService.getCinemaById(cinemaId));
    }

    @GetMapping("/name/{cinemaName}")
    public ResponseEntity<?> getCinemaByName(@PathVariable("cinemaName") String cinemaName) {
        return ResponseEntity.ok(cinemaService.getCinemaByName(cinemaName));
    }

    @PostMapping
    public ResponseEntity<?> registerCinema(@Valid  @RequestBody CinemaRegistrationRequest cinemaRegistrationRequest,
                                            BindingResult bindingResult) {
        List<String> errorMessage= new ArrayList<>();
        log.info("New cinema registration: {}", cinemaRegistrationRequest);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }
        cinemaService.registerCinema(cinemaRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/id-by-name/{cinemaName}")
    public ResponseEntity<Long> getCinemaIdByName(@PathVariable String cinemaName) {
        Long cinemaId = cinemaService.getCinemaIdByName(cinemaName);
        return ResponseEntity.ok(cinemaId);
    }


    @GetMapping("/{cinemaId}/exists")
    public ResponseEntity<?> existsById(@PathVariable long cinemaId) {
        boolean exists = cinemaService.existsById(cinemaId);
        if (!exists) {
            throw new ResourceNotFoundException("Cinema with id " + cinemaId + " not found");
        }
        return ResponseEntity.ok(true);
    }

}
