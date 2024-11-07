package com.movie.users.roles;

import com.movie.users.users.UserRowMapper;
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
    public Optional<Role> selectRoleById(Long role_id) {
        var sql = """
               SELECT role_id, role_name, description
               FROM roles where role_id=?
               """;

        return jdbcTemplate.query(sql,roleRowMapper,role_id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Role> selectRoleByName(String roleName) {
        var sql = """
               SELECT role_id, role_name, description
               FROM roles where role_name=?
               """;

        return jdbcTemplate.query(sql,roleRowMapper,roleName)
                .stream()
                .findFirst();
    }

}
