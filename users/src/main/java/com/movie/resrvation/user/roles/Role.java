package com.movie.resrvation.user.roles;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @SequenceGenerator(
            name= "role_id_sequence",
            sequenceName = "role_id_sequence"
    )
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
             generator = "role_id_sequence")
    private Integer roleId;
    private String roleName;
    private String description;
}
