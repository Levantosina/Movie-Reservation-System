package com.movie.users.roles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    @GetMapping("{roleId}")
    public RoleDTO getRole(@PathVariable ("roleId")Long roleId){
        return roleService.getRole(roleId);
    }
}
