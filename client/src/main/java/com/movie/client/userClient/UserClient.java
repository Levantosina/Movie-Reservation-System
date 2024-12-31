package com.movie.client.userClient;


import com.movie.common.UserDTO;
import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * @author DMITRII LEVKIN on 03/11/2024
 * @project Movie-Reservation-System
 */
@FeignClient(name = "user",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/users/userName/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);

    @GetMapping(value = "/api/v1/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long userId);
}