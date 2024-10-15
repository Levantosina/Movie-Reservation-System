package com.movie.users.roles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/roles")
public class RoleController {

    private  final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO>getAllRoles(){
        return roleService.getAllRoles();
    }

    @GetMapping("{roleId}")
    public RoleDTO getRole(@PathVariable ("roleId")Long roleId){
        return roleService.getRole(roleId);
    }

    @PostMapping("/validate")
    public ResponseEntity<Set<String>> validateRoles(@RequestBody Set<String> roleNames) {
        Set<String> validRoles = roleService.validateRoles(roleNames);

        if (validRoles.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptySet());
        }

        return ResponseEntity.ok(validRoles);
    }
}