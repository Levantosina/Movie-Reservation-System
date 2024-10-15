package com.movie.users.roles;

import com.movie.cinema.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Service
public class RoleService {

    private  final  RoleDAO roleDAO;
    private final RoleDTOMapper roleDTOMapper;


    public RoleService(@Qualifier("jdbcRole") RoleDAO roleDAO, RoleDTOMapper roleDTOMapper) {
        this.roleDAO = roleDAO;
        this.roleDTOMapper = roleDTOMapper;

    }

    public List<RoleDTO>getAllRoles(){
        return roleDAO.selectAllRoles()
                .stream()
                .map(roleDTOMapper)
                .collect(Collectors.toList());
    }
    public RoleDTO getRole(Long roleId){
        return  roleDAO.selectRoleById(roleId)
                .map(roleDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Role with id [%s] not found".
                                        formatted(roleId)));
    }

    public Set<String> validateRoles(Set<String> roleNames) {
        return roleNames.stream()
                .filter(roleName -> roleDAO.selectRoleByName(roleName).isPresent())
                .collect(Collectors.toSet());
    }


    public RoleDTO getRoleByRoleName(String roleName){
        return  roleDAO.selectRoleByName(roleName)
                .map(roleDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Role with id [%s] not found".
                                        formatted(roleName)));
    }

}

