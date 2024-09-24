package com.movie.resrvation.users;

import java.util.List;
import java.util.Optional;
/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
public interface UserDAO {

    List<User>selectAllUsers();
    Optional<User> selectUserById(Long id);
    Optional<User> selectUserByEmail(String email);
    boolean existPersonWithEmail(String email);
    boolean existUserWithId(Long id);
    void insertUser(User user);
    void deleteUserById(Long userId);
    void updateUser(User updateUser);

}
