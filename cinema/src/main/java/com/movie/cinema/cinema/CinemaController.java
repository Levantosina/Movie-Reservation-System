package com.movie.cinema.cinema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/cinemaId/{cinemaId}")
    public ResponseEntity<?> getCinemaById(@PathVariable("cinemaId") Long cinemaId) {

        return ResponseEntity.ok( cinemaService.getCinema(cinemaId));
    }

    @GetMapping("/name/{cinemaName}")
    public ResponseEntity<?> getCinemaByName(@PathVariable("cinemaName") String cinemaName) {
        return ResponseEntity.ok(cinemaService.getCinemaByName(cinemaName));
    }

    @PostMapping
    public ResponseEntity<?> registerCinema(@RequestBody CinemaRegistrationRequest cinemaRegistrationRequest) {
        log.info("New customer registration: {}", cinemaRegistrationRequest);
        cinemaService.registerCinema(cinemaRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/id-by-name/{cinemaName}")
    public ResponseEntity<Long> getCinemaIdByName(@PathVariable String cinemaName) {
        Long cinemaId = cinemaService.getCinemaIdByName(cinemaName);
        return ResponseEntity.ok(cinemaId);
    }
}
