package com.movie.users.users;

import com.movie.users.roles.Role;
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
    @SequenceGenerator(name="user_id_seq",
    sequenceName = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY,
    generator = "user_id_seq")
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
