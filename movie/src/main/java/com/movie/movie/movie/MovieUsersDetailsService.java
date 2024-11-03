package com.movie.movie.movie;

import com.movie.client.userClient.UserClient;
import com.movie.common.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author DMITRII LEVKIN on 03/11/2024
 * @project Movie-Reservation-System
 */
@Service
public class MovieUsersDetailsService implements UserDetailsService {
    private final UserClient userClient;

    public MovieUsersDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userResponse = userClient.getUserByUsername(username);
        if (userResponse == null) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userResponse.email(),
                "",
                new ArrayList<>()
        );
    }
}