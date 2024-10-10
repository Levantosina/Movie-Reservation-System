package com.movie.users.roles;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
public interface RoleDAO {

    List<Role> selectAllRoles();
    Optional<Role> selectRoleById(Long id);
}