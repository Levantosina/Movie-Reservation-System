package com.movie.resrvation.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Repository("jdbc")
public class UserAccessService implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    private final UserRowMapper userRowMapper;

    public UserAccessService(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> selectAllUsers() {
       var sql = """
              
               SELECT u.user_id, u.first_name, u.last_name, u.email,
                                 r.role_id, r.role_name, r.description
                          FROM users u
                          JOIN roles r ON u.role_id = r.role_id
               """;
       return jdbcTemplate.query(sql,userRowMapper);
    }

    @Override
    public Optional<User> selectUserById(Long id) {

        var sql = """
               SELECT user_id, first_name,last_name,email
               FROM users where user_id=?
               """;

        return jdbcTemplate.query(sql,userRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existUserWithId(Long id) {
        return false;
    }

    @Override
    public void insertUser(User user) {

        var sql = """
    INSERT INTO users (first_name, last_name, email, role_id)
    VALUES (?, ?, ?, ?)""";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().getRoleId());
    }

    @Override
    public void deleteUserById(Long userId) {

    }

    @Override
    public void updateUser(User updateUser) {

    }
}
