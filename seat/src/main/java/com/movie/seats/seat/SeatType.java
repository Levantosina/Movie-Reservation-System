package com.movie.seats.seat;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author DMITRII LEVKIN on 29/12/2024
 * @project Movie-Reservation-System
 */
@Getter
public enum SeatType {

    STANDARD("Standard", BigDecimal.valueOf(15.00)),
    VIP("VIP", BigDecimal.valueOf(22.00)),
    DISABLED("Disabled", BigDecimal.valueOf(10.00));

    private final String type;
    private final BigDecimal price;

    SeatType(String type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }

    public static BigDecimal getPriceByType(String type) {
        for (SeatType seatType : values()) {
            if (seatType.getType().equalsIgnoreCase(type)) {
                return seatType.getPrice();
            }
        }
        throw new IllegalArgumentException("Invalid seat type: " + type);
    }
}
