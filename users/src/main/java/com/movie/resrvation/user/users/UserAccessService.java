package com.movie.resrvation.user.users;

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
               SELECT user_id, first_name,last_name,email
               FROM users
               """;
       return jdbcTemplate.query(sql,userRowMapper);
    }

    @Override
    public Optional<User> selectUserById(Integer id) {

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
    public boolean existUserWithId(Integer id) {
        return false;
    }

    @Override
    public void insertUser(User user) {

    }

    @Override
    public void deleteUserById(Integer userId) {

    }

    @Override
    public void updateUser(User updateUser) {

    }
}
