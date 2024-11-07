package com.movie.seats.seat;

/**
 * @author DMITRII LEVKIN on 04/10/2024
 * @project MovieReservationSystem
 */
public class SeatInfo {

    private final int count;
    private final String type;

    public SeatInfo(int count, String type) {
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }
}
