package com.movie.client.notification;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */
@FeignClient(name="notification", url= "http://localhost:8086")
public interface NotificationClient {
    @RequestMapping(method = RequestMethod.POST, value ="api/v1/notification",consumes = "application/json")
    void sendNotification(NotificationRequest notificationRequest);
}

