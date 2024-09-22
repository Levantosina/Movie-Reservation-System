package com.movie.resrvation.user;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
