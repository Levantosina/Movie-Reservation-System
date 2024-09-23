package com.movie.resrvation.user.roles;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Service
public class RoleDTOMapper  implements Function<Role, RoleDTO> {
    @Override
    public RoleDTO apply(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription()
        );
    }
}
