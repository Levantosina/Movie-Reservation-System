package com.movie.seats.seat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long seatId;
    @NotNull
    @Column(
            nullable = false
    )
    private Integer seatNumber;
    @NotNull
    @Column(
            nullable = false
    )

    private String row;
    @NotNull
    @Column(
            nullable = false
    )

    private String type;

    @Column(name = "cinema_id")
    private Long cinemaId;

    @Column(name = "is_occupied")
    private boolean isOccupied;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(seatId, seat.seatId) && Objects.equals(seatNumber, seat.seatNumber) && Objects.equals(row, seat.row) && Objects.equals(type, seat.type) && Objects.equals(cinemaId, seat.cinemaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, seatNumber, row, type, cinemaId);
    }

    public boolean isOccupied() {
        return isOccupied;
    }


}