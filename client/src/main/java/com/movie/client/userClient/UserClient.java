package com.movie.client.userClient;

import com.movie.common.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DMITRII LEVKIN on 03/11/2024
 * @project Movie-Reservation-System
 */
@FeignClient(name = "users") // Change to your user service name
public interface UserClient {

    @GetMapping("/api/v1/users/userName/{username}")
    UserDTO getUserByUsername(@RequestParam("username") String username);
}