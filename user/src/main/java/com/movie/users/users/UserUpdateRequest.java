package com.movie.users.users;

/**
 * @author DMITRII LEVKIN on 14/10/2024
 * @project MovieReservationSystem
 */
public record UserUpdateRequest (
        String firstName,
        String lastName,
        String email
) {
}
