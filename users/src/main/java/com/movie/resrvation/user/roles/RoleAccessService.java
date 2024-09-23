package com.movie.resrvation.user.roles;

import com.movie.resrvation.user.users.User;
import com.movie.resrvation.user.users.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 23/09/2024
 * @project MovieReservationSystem
 */
@Repository("jdbcRole")
public class RoleAccessService implements RoleDAO {
    private  final JdbcTemplate jdbcTemplate;
    private final RoleRowMapper roleRowMapper;

    public RoleAccessService(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper, RoleRowMapper roleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;

        this.roleRowMapper = roleRowMapper;
    }

    @Override
    public List<Role> selectAllRoles() {
        var sql = """
               SELECT role_id, role_name, description
               FROM roles
               """;
        return jdbcTemplate.query(sql,roleRowMapper);
    }

    @Override
    public Optional<Role> selectRoleById(Integer role_id) {
        var sql = """
               SELECT role_id, role_name, description
               FROM roles where role_id=?
               """;

        return jdbcTemplate.query(sql,roleRowMapper,role_id)
                .stream()
                .findFirst();
    }
}
