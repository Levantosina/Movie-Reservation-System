package com.movie.users.users;



import com.movie.jwt.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author DMITRII LEVKIN on 14/10/2024
 * @project MovieReservationSystem
 */

@RestController
@Slf4j
@RequestMapping("api/v1/admin")
public class AdminController { ///only admin

    private final AdminService adminService;

    private final JWTUtil jwtUtil;

    public AdminController(AdminService adminService, JWTUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest adminRegistrationRequest,
                                           @AuthenticationPrincipal User currentAdmin){
        log.info("New ADMIN registration: {}", adminRegistrationRequest);
        adminService.registerAdministrator(adminRegistrationRequest,currentAdmin);
        String jwtToken= jwtUtil.issueToken(adminRegistrationRequest.email(),adminRegistrationRequest.roleName());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,jwtToken)
                .build();

    }


    @PostMapping("/users/{username}/roles")
    public ResponseEntity<?> updateUserRoles(
            @PathVariable String username,
            @RequestBody Set<String> roles) {


        adminService.updateUserRoles(username, roles);

        return ResponseEntity.ok("User roles updated successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetAdminPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        adminService.resetAdminPassword(passwordResetRequest.userName(), passwordResetRequest.newPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}
