package com.movie.cinema.cinema;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


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
    @SequenceGenerator(
            name = "cinemas_id_seq",
            sequenceName = "cinemas_id_seq"
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
    generator = "cinemas_id_seq")
    private Long cinemaId;
    @Column(name = "cinema_name")
    private String cinemaName;
    @Column(name = "cinema_location")
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
