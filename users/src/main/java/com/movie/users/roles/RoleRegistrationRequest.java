package com.movie.users.roles;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
public record RoleRegistrationRequest(
        String roleName,
        String description
) { }
