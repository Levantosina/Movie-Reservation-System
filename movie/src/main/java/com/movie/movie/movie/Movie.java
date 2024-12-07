package com.movie.movie.movie;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

/**
 * @author DMITRII LEVKIN on 30/09/2024
 * @project MovieReservationSystem
 */

@Entity
@Table(name = "movies",
        uniqueConstraints = {
                @UniqueConstraint(name = "movie_movieName_unique",
                        columnNames = "movieName")
        })
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
            sequenceName = "movies_id_seq"
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            nullable = false
    )
    private Long movieId;
    @NotNull
    @Column(
            nullable = false
    )
    private String movieName;
    @NotNull
    @Column(
            nullable = false
    )
    private Integer year;
    @NotNull
    @Column(
            nullable = false
    )
    private String country;
    @NotNull
    @Column(
            nullable = false
    )
    private String genre;
    @NotNull
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
