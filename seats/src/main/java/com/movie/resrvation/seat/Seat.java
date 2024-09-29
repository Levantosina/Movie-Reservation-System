package com.movie.resrvation.seat;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author DMITRII LEVKIN on 25/09/2024
 * @project MovieReservationSystem
 */
@Entity
@Table(name = "seats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    private String seatNumber;

    private String row;

    private String type;
    @Column(name = "cinema_id")
    private Long cinemaId;

}
