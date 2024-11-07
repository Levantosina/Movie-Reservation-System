package com.movie.users.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Repository("userJdbc")
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
              
               SELECT u.user_id, u.first_name, u.last_name, u.email,u.password,
                                 r.role_id, r.role_name, r.description
                          FROM users u
                          JOIN roles r ON u.role_id = r.role_id
               """;
        return jdbcTemplate.query(sql,userRowMapper);
    }



    @Override
    public Optional<User> selectUserById(Long id) {

        var sql = """
                SELECT u.user_id, u.first_name, u.last_name, u.email,u.password,
                                 r.role_id, r.role_name, r.description
                          FROM users u
                          JOIN roles r ON u.role_id = r.role_id where user_id=?
               """;

        return jdbcTemplate.query(sql,userRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        var sql = """
           SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, r.role_id, r.role_name, r.description
           FROM users u
           JOIN roles r ON u.role_id = r.role_id
           WHERE u.email = ?
           """;

        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst();
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
    INSERT INTO users (first_name, last_name, email, password, role_id)
    VALUES (?, ?, ?, ?, ?) RETURNING user_id
    """;

        Long userId = jdbcTemplate.queryForObject(
                sql, Long.class,
                user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(),
                user.getRole().getRoleId()
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