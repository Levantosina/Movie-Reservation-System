package com.movie.users.users;



import com.movie.common.UserDTO;
import com.movie.jwt.jwt.JWTUtil;
import com.movie.users.users.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal User currentAdmin) {
        List<UserDTO> users = adminService.getAllUsers();
        String jwtToken = jwtUtil.issueToken(currentAdmin.getUsername(), "ROLE_ADMIN");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(users);
    }

    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest adminRegistrationRequest,
                                           @AuthenticationPrincipal User currentAdmin){
        log.info("New ADMIN registration: {}", adminRegistrationRequest);

        if (!currentAdmin.getRole().equals(Role.ROLE_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only administrators can register other admins.");
        }
        adminService.registerAdministrator(adminRegistrationRequest,currentAdmin);
        String jwtToken= jwtUtil.issueToken(adminRegistrationRequest.email(),"ROLE_ADMIN");

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION,jwtToken)
                .build();

    }


    @PostMapping("/users/{email}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable String email, @RequestBody RoleRequestDTO roleDto) {
        try {
            adminService.updateUserRoles(email, roleDto.roleName());
            return ResponseEntity.ok("User role updated successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user role");
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetAdminPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        adminService.resetAdminPassword(passwordResetRequest.userName(), passwordResetRequest.newPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        try {
            UserDTO admin = adminService.getAdminById(id);
            return ResponseEntity.ok(admin);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving admin");
        }
    }
}
