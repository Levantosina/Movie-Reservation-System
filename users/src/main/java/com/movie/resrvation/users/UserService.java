package com.movie.resrvation.users;

import com.movie.resrvation.roles.Role;

import com.movie.resrvation.roles.RoleDAO;
import com.movie.resrvation.users.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Service
public class UserService {

    private final UserDAO userDAO;
    private final UserDTOMapper userDTOMapper;

    private final RoleDAO roleDAO;


    public UserService(@Qualifier("jdbc") UserDAO userDAO, UserDTOMapper userDTOMapper,
                       RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;

        this.roleDAO = roleDAO;
    }

    public List<UserDTO> getAllUsers() {
        return userDAO.selectAllUsers()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        return userDAO.selectUserById(id)
                .map(userDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(id)));
    }

    public void registerUser(UserRegistrationRequest userRegistrationRequest){

        Role role = roleDAO.selectRoleById(userRegistrationRequest.roleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());
        user.setRole(role);

        userDAO.insertUser(user);

    }
}