package com.movie.notification;


import com.movie.client.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */
@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;



    public void send(NotificationRequest notificationRequest) {

        System.out.println("Sending notification to user ID: " + notificationRequest.toUserId());

        notificationRepository.save(
                Notification.builder()
                        .toUserId(notificationRequest.toUserId())
                        .toUserEmail(notificationRequest.toUserEmail())
                        .sender("Levantos")
                        .message(notificationRequest.message())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}

