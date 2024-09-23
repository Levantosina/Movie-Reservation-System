package com.movie.resrvation.user.users;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserDTO(
        Integer userId,
        String firstName,
        String lastName,
        String email

) {
}
