package com.movie.users.users;

import com.movie.users.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {

        log.info("New customer registration: {}", userRegistrationRequest);
        userService.registerUser(userRegistrationRequest);
       String jwtToken= jwtUtil.issueToken(userRegistrationRequest.email(),userRegistrationRequest.roleId());

      return ResponseEntity.ok()
               .header(HttpHeaders.AUTHORIZATION,jwtToken)
              .build();
    }
}
