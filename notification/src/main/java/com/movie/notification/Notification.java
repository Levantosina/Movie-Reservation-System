package com.movie.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author DMITRII LEVKIN on 07/10/2024
 * @project MovieReservationSystem
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @SequenceGenerator(
            name = "notification_id_sequence",
            sequenceName = "notification_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence"
    )
    private Long notificationId;
    private Long toUserId;
    private String toUserEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
}
