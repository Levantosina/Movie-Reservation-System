package com.movie.resrvation.user;

import com.movie.resrvation.exception.ResourceNotFoundException;
import com.movie.resrvation.user.UserDAO;
import com.movie.resrvation.user.UserDTO;
import com.movie.resrvation.user.UserDTOMapper;
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

    public UserService(@Qualifier("jdbc") UserDAO userDAO, UserDTOMapper userDTOMapper, UserRepository userRepository) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
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
        User user= User.builder()
                .firstName(userRegistrationRequest.firstName())
                .lastName(userRegistrationRequest.lastName())
                .email(userRegistrationRequest.email())
                .build();
        userRepository.saveAndFlush(user);

    }
}
