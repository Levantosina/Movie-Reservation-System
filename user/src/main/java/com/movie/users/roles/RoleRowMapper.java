package com.movie.users.roles;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Component
public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Role(
                rs.getLong("role_id"),
                rs.getString("role_name"),
                rs.getString("description")
        );
    }
}