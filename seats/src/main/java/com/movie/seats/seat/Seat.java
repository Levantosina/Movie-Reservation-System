package com.movie.seats.seat;

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
    @SequenceGenerator(
            name = "seats_id_seq",
            sequenceName = "seats_id_seq"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "seats_id_seq")
    private Long seatId;

    private String seatNumber;

    private String row;

    private String type;
    @Column(name = "cinema_id")
    private Long cinemaId;

}
