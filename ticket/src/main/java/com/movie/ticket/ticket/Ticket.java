package com.movie.ticket.ticket;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@Entity
@Table(name = "ticket")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @SequenceGenerator(
            name = "ticket_id_seq",
            sequenceName = "ticket_id_seq"
    )
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long ticketId;
    @NotNull
    @Column(
            nullable = false
    )
    private Long userId;
    @NotNull
    @Column(
            nullable = false
    )
    private Long movieId;
    @NotNull
    @Column(
            nullable = false
    )
    private Long cinemaId;
    @NotNull
    @Column(
            nullable = false
    )
    private Long seatId;
    @NotNull
    @Column(
            nullable = false
    )
    private Long scheduleId;
    @NotNull
    @Column(
            nullable = false
    )
    private BigDecimal price;
    @NotNull
    @Column(
            nullable = false
    )
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId) && Objects.equals(userId, ticket.userId) && Objects.equals(movieId, ticket.movieId) && Objects.equals(cinemaId, ticket.cinemaId) && Objects.equals(seatId, ticket.seatId) && Objects.equals(scheduleId, ticket.scheduleId) && Objects.equals(price, ticket.price) && Objects.equals(date, ticket.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, userId, movieId, cinemaId, seatId, scheduleId, price, date);
    }
}
