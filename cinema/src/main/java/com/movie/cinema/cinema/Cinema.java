package com.movie.cinema.cinema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;


/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */

@Entity
@Table(name = "cinemas"
        , uniqueConstraints = {
        @UniqueConstraint(name = "cinema_name_unique",
                columnNames = "cinemaName")})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {

    @Id
    @SequenceGenerator(
            name = "cinemas_id_seq",
            sequenceName = "cinemas_id_seq"
    )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long cinemaId;
    @NotNull
    @Column(
            nullable = false
    )
    private String cinemaName;
    @NotNull
    @Column(
            nullable = false
    )
    private String cinemaLocation;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema cinema = (Cinema) o;
        return Objects.equals(cinemaId, cinema.cinemaId) && Objects.equals(cinemaName, cinema.cinemaName) && Objects.equals(cinemaLocation, cinema.cinemaLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cinemaId, cinemaName, cinemaLocation);
    }
}
