package com.movie.resrvation.theater;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
public interface TheaterDAO {

    List<Theater> selectAllTheaters();
    Optional<Theater> selectTheaterById(Long theaterId);
    void insertTheater(Theater theater);
    boolean existTheaterWithName(String name);

    void updateTheater(Theater updateTheater);
}
