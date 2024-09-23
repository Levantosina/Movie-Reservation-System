package com.movie.resrvation.user.roles;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
public interface RoleDAO {

    List<Role> selectAllRoles();
    Optional<Role> selectRoleById(Integer id);
}
