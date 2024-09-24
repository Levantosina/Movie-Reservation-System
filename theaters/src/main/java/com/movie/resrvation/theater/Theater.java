package com.movie.resrvation.theater;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */

@Entity
@Table(name = "theaters")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long theaterId;
    private String theaterName;
    private String theaterLocation;
}
