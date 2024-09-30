package com.movie.resrvation.movie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/movies")
public class MovieController {

    private  final  MovieService movieService;

    public MovieController(MovieService movieService) {

        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO>getAllMovie(){

        return movieService.getAllMovies();
    }

    @GetMapping("{movieId}")
    public MovieDTO getMovie(@PathVariable("movieId") Long movieId){
        return movieService.getMovie(movieId);
    }

    @PostMapping
    public void registerMovie(@RequestBody MovieRegistrationRequest movieRegistrationRequest){
        log.info("New movie registration: {}", movieRegistrationRequest);
        movieService.registerNewMovie(movieRegistrationRequest);
    }
}
