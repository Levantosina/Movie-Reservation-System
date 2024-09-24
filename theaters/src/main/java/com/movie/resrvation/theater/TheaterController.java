package com.movie.resrvation.theater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public List<TheaterDTO> getAllTheaters(){
        return theaterService.getAllTheaters();
    }
    @GetMapping("{theaterId}")
    public TheaterDTO getTheater(@PathVariable("theaterId")Long theaterId){

        return theaterService.getTheater(theaterId);
    }

    @PostMapping
    public void registerTheater(@RequestBody TheaterRegistrationRequest theaterRegistrationRequest) {
        log.info("New customer registration: {}", theaterRegistrationRequest);
        theaterService.registerTheater(theaterRegistrationRequest);
    }
}
