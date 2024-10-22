package com.movie.users.auth;


import com.movie.common.AuthenticationRequest;
import com.movie.common.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
@RestController
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;



    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }


    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        log.info("Authenticating user: {}", request.username());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,response.token())
                .body(response);
    }
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.split(" ")[1]; // Extract token part from "Bearer token"
        boolean isValid = authenticationService.validateToken(jwtToken);

        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(403).body("Invalid token");
        }
    }
}