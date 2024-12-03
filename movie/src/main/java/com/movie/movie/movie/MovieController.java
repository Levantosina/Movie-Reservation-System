package com.movie.movie.movie;



import com.movie.jwt.jwt.JWTUtil;
import com.movie.users.users.UserRegistrationRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final MovieService movieService;



    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @GetMapping
    public ResponseEntity<?> getMovies() {
            return ResponseEntity.ok( movieService.getAllMovies());
    }


    @GetMapping("{movieId}")
    public ResponseEntity<?> getMovie(@PathVariable("movieId") Long movieId) {

        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @PostMapping
    public ResponseEntity<?> registerMovie(@RequestBody MovieRegistrationRequest movieRegistrationRequest) {
        log.info("New movie registration: {}", movieRegistrationRequest);
        movieService.registerNewMovie(movieRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{movieId}")
    public ResponseEntity<?> updateMovieInfo(@PathVariable("movieId") Long movieId,@RequestBody MovieUpdateRequest movieUpdateRequest){
        log.info("Update movie: {}",movieUpdateRequest);
        movieService.updateMovie(movieId,movieUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
