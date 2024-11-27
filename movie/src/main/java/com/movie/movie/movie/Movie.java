package com.movie.movie.movie;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */

@Entity
@Table(name = "movies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @SequenceGenerator(
            name = "movies_id_seq",
            sequenceName = "movies_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY,
                    generator = "movies_id_seq")
    @Column(
            nullable = false
    )
    private Long movieId;
    @Column(
            nullable = false
    )
    private String movieName;
    @Column(
            nullable = false
    )
    private Integer year;
    @Column(
            nullable = false
    )
    private String country;
    @Column(
            nullable = false
    )
    private String genre;
    @Column(
            nullable = false
    )
    private String description;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(movieId, movie.movieId) && Objects.equals(movieName, movie.movieName) && Objects.equals(year, movie.year) && Objects.equals(country, movie.country) && Objects.equals(genre, movie.genre) && Objects.equals(description, movie.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieName, year, country, genre, description);
    }
}
