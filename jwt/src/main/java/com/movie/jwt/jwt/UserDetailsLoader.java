package com.movie.jwt.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author DMITRII LEVKIN on 17/10/2024
 * @project Movie-Reservation-System
 */
public interface UserDetailsLoader {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
