package com.movie.cinema.cinema;





import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
public interface CinemaDAO {

    List<Cinema> selectAllCinemas();
    Optional<Cinema> selectCinemaById(Long CinemaId);

    Optional<Cinema>selectCinemaByCinemaName(String CinemaName);
    void deleteCinemaById(Long CinemaId);

    void insertCinema(Cinema cinema);
    boolean existsById(Long cinemaId);
    void updateCinema(Cinema cinema);
}
