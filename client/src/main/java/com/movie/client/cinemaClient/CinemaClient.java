package com.movie.client.cinemaClient;





import com.movie.common.CinemaDTO;
import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(value = "cinema",configuration = FeignConfig.class)

public interface CinemaClient {
    @GetMapping(value= "/api/v1/cinemas/id-by-name/{cinemaName}")
    Long getCinemaIdByName(@PathVariable("cinemaName") String cinemaName);

    @GetMapping(value = "/api/v1/cinemas/{id}")
    CinemaDTO getCinemaById(@PathVariable("id") Long cinemaId);

    @GetMapping("/api/v1/cinemas/{cinemaId}/exists")
    boolean existsById(@PathVariable long cinemaId);

}


