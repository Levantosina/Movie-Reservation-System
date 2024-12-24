package com.movie.client.movieClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@FeignClient(name = "movies")
public interface MovieClient {

    @GetMapping(value = "/api/v1/movies/{id}")
    Long getMovieById(@PathVariable("id") Long movieId);
}
