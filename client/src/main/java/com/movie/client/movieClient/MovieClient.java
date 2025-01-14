package com.movie.client.movieClient;


import com.movie.common.MovieDTO;
import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@FeignClient(name = "movie",configuration = FeignConfig.class)
public interface MovieClient {

    @GetMapping(value = "/api/v1/movies/{movieId}")
    MovieDTO getMovieById(@PathVariable("movieId") Long movieId);

    @GetMapping("/api/v1/movies/{movieId}/exists")
    boolean existsById(@PathVariable long movieId);
}
