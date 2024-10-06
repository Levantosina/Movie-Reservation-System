package com.movie.notification;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
