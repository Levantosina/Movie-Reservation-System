package com.movie.client.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(
        name="notification",
        url= "${client.notification.url}"
)

public interface NotificationClient {
    @PostMapping("api/v1/notification")
    void sendNotification(NotificationRequest notificationRequest);
}

