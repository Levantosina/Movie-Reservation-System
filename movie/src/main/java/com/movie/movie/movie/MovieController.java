package com.movie.movie.movie;




import com.movie.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
    public ResponseEntity<MovieDTO> getMovie(@PathVariable("movieId") Long movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @GetMapping("/{movieId}/name")
    public ResponseEntity<String> getMovieNameById(@PathVariable Long movieId) {
        String movieName = movieService.getMovieNameById(movieId);
        return ResponseEntity.ok(movieName);
    }

    @PostMapping
    public ResponseEntity<?> registerMovie(@Valid  @RequestBody MovieRegistrationRequest movieRegistrationRequest,
                                           BindingResult bindingResult) {
        List<String> errorMessage= new ArrayList<>();
        log.info("New movie registration: {}", movieRegistrationRequest);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }
            movieService.registerNewMovie(movieRegistrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("{movieId}")
    public ResponseEntity<?> updateMovieInfo(@PathVariable("movieId") Long movieId,@RequestBody MovieUpdateRequest movieUpdateRequest){
        log.info("Update movie: {}",movieUpdateRequest);
        movieService.updateMovie(movieId,movieUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{movieId}/exists")
    public ResponseEntity<?> existsById(@PathVariable Long movieId) {
        movieService.existsById(movieId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(true);
    }
    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long movieId) {
        log.info("Delete movie: {}",movieId);
        movieService.deleteMovieById(movieId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
