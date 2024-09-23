package com.movie.resrvation.user.users;

import com.movie.resrvation.user.roles.Role;
import com.movie.resrvation.user.roles.RoleRepository;
import com.movie.resrvation.user.users.exception.ResourceNotFoundException;
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

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(@Qualifier("jdbc") UserDAO userDAO, UserDTOMapper userDTOMapper, UserRepository userRepository, RoleRepository roleRepository) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userDAO.selectAllUsers()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Integer id) {
        return userDAO.selectUserById(id)
                .map(userDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(id)));
    }
    public void registerUser(UserRegistrationRequest userRegistrationRequest){

        Role role = roleRepository.findById(userRegistrationRequest.roleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());
        user.setRole(role); // Set the fetched role

        userRepository.save(user);

    }
}
