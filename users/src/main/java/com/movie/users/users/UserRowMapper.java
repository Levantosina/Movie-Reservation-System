package com.movie.users.users;

import com.movie.users.roles.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
       Role role= new Role(
               rs.getLong("role_id"),
               rs.getString("role_name"),
               rs.getString("description")
       );

        return User.builder()
                .userId(rs.getLong("user_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .role(role)
                .build();
    }
}
