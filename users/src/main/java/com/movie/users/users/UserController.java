package com.movie.users.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<UserDTO> getUsers(){

        return userService.getAllUsers();
    }
    @GetMapping("{userId}")
    public UserDTO getCustomer(@PathVariable("userId")Long userId){
        return userService.getUser(userId);
    }

    @PostMapping
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("New customer registration: {}", userRegistrationRequest);
        userService.registerUser(userRegistrationRequest);
    }
}
