package com.movie.seats.seat;


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
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/seats")
public class SeatController {

    private final  SeatService seatService;

    @PostMapping
    public ResponseEntity<?> registerNewSeat(@Valid  @RequestBody SeatRegistrationRequest seatRegistrationRequest, BindingResult bindingResult){
        List<String> errorMessage= new ArrayList<>();
        log.info("New seat registration: {}", seatRegistrationRequest);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }
            seatService.registerNewSeat(seatRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public SeatController(SeatService seatService) {

        this.seatService = seatService;
    }
    @GetMapping
    public List<SeatDTO> getAllSeats(){
        return seatService.getAllSeats();
    }

    @GetMapping("/{seatId}")
    public ResponseEntity<SeatDTO> getSeat(@PathVariable("seatId") Long seatId) {
        SeatDTO seatDTO = seatService.getSeat(seatId);
        return ResponseEntity.ok(seatDTO);
    }

    @GetMapping("/cinema/{cinemaId}")
    public List<SeatDTO> getAllSeatsByCinemaId(@PathVariable("cinemaId") Long cinemaId) {
        log.info("Fetching seats for cinema ID: {}", cinemaId);
        return seatService.getSeatsByCinema(cinemaId);
    }

    @PostMapping("/cinema")
    public List<SeatDTO> getSeatsByCinema(@RequestParam("cinemaId") Long cinemaId) {
        log.info("Fetching seats for cinema ID: {}", cinemaId);
        return seatService.getSeatsByCinema(cinemaId);
    }

    @GetMapping("/cinema/{cinemaId}/total-seats")
    public int getTotalSeatsByCinemaId(@PathVariable Long cinemaId) {
        log.info("Fetching total seats for cinema ID: {}", cinemaId);
        return seatService.getTotalSeatsByCinemaId(cinemaId);
    }
    @GetMapping("/{seatId}/is-occupied")
    public ResponseEntity<?> checkSeatOccupation(@PathVariable("seatId") Long seatId) {
        boolean isOccupied = seatService.isSeatOccupied(seatId);
        return ResponseEntity.ok(isOccupied);
    }

    @PostMapping("/{seatId}/update-occupation")
    public ResponseEntity<?> updateSeatOccupation(@PathVariable("seatId") Long seatId, @RequestBody boolean isOccupied) {
        seatService.updateSeatOccupation(seatId, isOccupied);
        return ResponseEntity.ok(isOccupied);
    }

    @PutMapping("{seatId}")
    public ResponseEntity<?> updateSeats(@PathVariable("seatId")Long seatId,@RequestBody SeatUpdateRequest seatUpdateRequest){
        log.info("Update seat: {}",seatUpdateRequest);
        seatService.updateSeat(seatId,seatUpdateRequest);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

}
