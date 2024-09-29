package com.movie.resrvation.cinema;

import jakarta.persistence.*;
import lombok.*;


/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */

@Entity
@Table(name = "cinemas")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long cinemaId;
    @Column(name = "cinema_name")
    private String cinemaName;
    @Column(name = "cinema_location")
    private String cinemaLocation;
}
