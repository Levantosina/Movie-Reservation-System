package com.movie.resrvation.users;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Long roleId

        ) {
}