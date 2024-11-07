package com.movie.schedules.movieschedules;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author DMITRII LEVKIN on 01/10/2024
 * @project MovieReservationSystem
 */
@Entity
@Table(name = "schedules")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieSchedule {
    @Id
    @SequenceGenerator(
            name = "schedule_id_seq",
            sequenceName = "schedule_id_seq"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_id_seq")
    private Long scheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer availableSeats;
    private Long cinemaId;
    private Long movieId;
}
