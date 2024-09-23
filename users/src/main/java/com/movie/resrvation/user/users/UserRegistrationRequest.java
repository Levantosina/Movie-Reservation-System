package com.movie.resrvation.user.users;

import java.math.BigInteger;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Integer roleId

        ) {
}
