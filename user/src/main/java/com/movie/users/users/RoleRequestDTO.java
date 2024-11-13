package com.movie.users.users;



/**
 * @author DMITRII LEVKIN on 13/11/2024
 * @project Movie-Reservation-System
 */

public record RoleRequestDTO(String roleName) {
    @Override
    public String roleName() {
        return roleName;
    }
}
