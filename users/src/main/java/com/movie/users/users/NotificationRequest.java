package com.movie.users.users;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */
public record NotificationRequest(
        Long toUserId,
        String toUserEmail,
        String message
        ) {
}
