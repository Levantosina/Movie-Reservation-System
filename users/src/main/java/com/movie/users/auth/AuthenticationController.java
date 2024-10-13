package com.movie.users.auth;

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
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
@PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
      AuthenticationResponse response = authenticationService.login(request);

      return ResponseEntity.ok()
              .header(HttpHeaders.AUTHORIZATION,response.token())
              .body(response);
    }
}
