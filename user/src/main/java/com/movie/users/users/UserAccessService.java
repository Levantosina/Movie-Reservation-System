package com.movie.users.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Repository("userJdbc")
@Slf4j
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
                SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, u.role_name
                FROM users u
                """;
        return jdbcTemplate.query(sql, userRowMapper);
    }



    @Override
    public Optional<User> selectUserById(Long id) {
        var sql = """
                SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, u.role_name
                FROM users u
                WHERE u.user_id = ?
                """;
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst();
    }


    @Override
    public Optional<User> selectUserByEmail(String email) {
        var sql = """
            
                SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, u.role_name
            FROM users u
            WHERE u.email = ?
            """;
        try {
            return jdbcTemplate.query(sql, userRowMapper, email)
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            throw new RuntimeException("Error fetching user by email", e);
        }
    }



    @Override
    public boolean existPersonWithEmail(String email) {

        var sql= """
               SELECT count(user_id)FROM users
               where email=?
               """;

        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count!=null && count>0;
    }

    @Override
    public boolean existUserWithId(Long id) {

        var sql= """
                SELECT count(user_id)FROM users
                where user_id=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count!=null && count>0;
    }

    @Override
    public void insertUser(User user) {
        var sql = """
            INSERT INTO users (first_name, last_name, email, password, role_name)
            VALUES (?, ?, ?, ?, ?) RETURNING user_id
            """;

        Long userId = jdbcTemplate.queryForObject(
                sql, Long.class,
                user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(),
                user.getRole().name()
        );
        user.setUserId(userId);
    }

    @Override
    public void updateUser(User updateUser) {

        if(updateUser.getFirstName()!=null){
            var sql = """
                    UPDATE users SET first_name=? where user_id=?
                    """;
             jdbcTemplate.update(
                    sql,
                    updateUser.getFirstName(),
                    updateUser.getUserId()
            );

        }

        if(updateUser.getLastName()!=null){
            var sql = """
                    UPDATE users SET last_name=? where user_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateUser.getLastName(),
                    updateUser.getUserId()
            );

        }

        if(updateUser.getEmail()!=null){
            var sql = """
                    UPDATE users SET email=? where user_id=?
                    """;
            jdbcTemplate.update(
                    sql,
                    updateUser.getEmail(),
                    updateUser.getUserId()
            );

        }

        if (updateUser.getRole() != null) {
            var sql = """
                    UPDATE users SET role_name = ? WHERE user_id = ?
                    """;
            jdbcTemplate.update(sql, updateUser.getRole().name(), updateUser.getUserId());
        }

    }

    @Override
    public Optional<User> selectRoleByName(String role) {
        var sql= """
                SELECT * FROM users WHERE role_name = ?
                """;

        return jdbcTemplate.query(sql, userRowMapper, role)
                .stream()
                .findFirst();

    }

    @Override
    public void insert(Role role) {
        var sql = """
                INSERT INTO users (role_name) VALUES (?)
                """;

        jdbcTemplate.update(sql, role.name());
    }

    @Override
    public void deleteUserById(Long userId) {

            var sql = """
                    DELETE FROM users where user_id = ?
                    """;

            int result= jdbcTemplate.update(sql,userId);
        System.out.println(" Deleted = " + result);
    }
}