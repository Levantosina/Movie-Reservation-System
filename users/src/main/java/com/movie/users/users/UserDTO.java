package com.movie.users.users;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,

        String roleName


) {
}