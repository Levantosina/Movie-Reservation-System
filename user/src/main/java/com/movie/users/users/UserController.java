package com.movie.users.users;



import com.movie.common.UserDTO;
import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.ResourceNotFoundException;
import com.movie.jwt.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        try {
            UserDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("New user registration: {}", userRegistrationRequest);

        try {

            userService.registerUser(userRegistrationRequest);

            String jwtToken = jwtUtil.issueToken(userRegistrationRequest.email(), "ROLE_USER");


            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
                    .body("User registered successfully");
        } catch (DuplicateResourceException ex) {
            log.error("Error during registration: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error during registration", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PutMapping("{userId}")
    public void updateUser(@PathVariable("userId") Long userId,@RequestBody UserUpdateRequest userUpdateRequest){
        log.info(" Updated user: {}",userUpdateRequest);
        userService.updateUser(userId,userUpdateRequest);
    }

    @DeleteMapping("{userId}")
    public  void  deleteUser(@PathVariable("userId")Long userId){
        log.info("Deleted user: {}", userId);
        userService.deleteUserById(userId);
    }

    @GetMapping("/userName/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
        try {
            UserDTO user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}