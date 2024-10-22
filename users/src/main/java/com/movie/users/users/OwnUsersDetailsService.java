package com.movie.users.users;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author DMITRII LEVKIN on 09/10/2024
 * @project MovieReservationSystem
 */
@Service
public class OwnUsersDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    public OwnUsersDetailsService(@Qualifier("userJdbc") UserDAO userDAO) {

        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.selectUserByEmail(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("Username "+username+" not found"));
    }
}