package com.movie.resrvation.user;

import jakarta.persistence.*;
import lombok.*;
/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence")
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence")
    private Integer userId;
    private String firstName;

    private String lastName;
    private String email;

}
