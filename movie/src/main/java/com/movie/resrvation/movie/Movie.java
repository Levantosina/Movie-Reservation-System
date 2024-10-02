package com.movie.resrvation.movie;

import jakarta.persistence.*;
import lombok.*;

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
            sequenceName = "movies_id_seq"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "movies_id_seq")
    private Long movieId;
    private String movieName;
    private Integer year;
    private String country;
    private String genre;
    private String description;


}
