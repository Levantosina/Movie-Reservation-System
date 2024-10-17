package com.movie.users.auth;


import com.movie.common.AuthenticationRequest;
import com.movie.common.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}