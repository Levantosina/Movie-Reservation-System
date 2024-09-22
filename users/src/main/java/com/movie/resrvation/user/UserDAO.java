package com.movie.resrvation.user;

import com.movie.resrvation.user.User;

import java.util.List;
import java.util.Optional;
/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public interface UserDAO {

    List<User>selectAllUsers();
    Optional<User> selectUserById(Integer id);
    Optional<User> selectUserByEmail(String email);
    boolean existPersonWithEmail(String email);
    boolean existUserWithId(Integer id);
    void insertUser(User user);
    void deleteUserById(Integer userId);
    void updateUser(User updateUser);

}
