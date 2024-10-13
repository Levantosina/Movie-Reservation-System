package com.movie.users.auth;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
public record AuthenticationRequest (
        String username,
        String password)
{ }
