package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;


import com.movie.client.notification.NotificationRequest;
import com.movie.common.UserDTO;


import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 22/09/2024
 * @project MovieReservationSystem
 */
@Service
@Slf4j
public class UserService {

    private final UserDAO userDAO;
    private final UserDTOMapper userDTOMapper;



    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    private final PasswordEncoder passwordEncoder;


    public UserService(@Qualifier("userJdbc") UserDAO userDAO, UserDTOMapper userDTOMapper,
                        RabbitMqMessageProducer rabbitMqMessageProducer, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userDAO.selectAllUsers()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userDAO.selectUserById(id)
                .map(userDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User with id [%s] not found".
                                        formatted(id)));
    }
    public UserDTO getUserByUsername(String username) {
        return userDAO.selectUserByEmail(username)
                .map(userDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User with username [%s] not found".
                                        formatted(username)));
    }

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        Optional<User> existingUser = userDAO.selectUserByEmail(userRegistrationRequest.email());
        if (existingUser.isPresent()) {
            throw new DuplicateResourceException("Email is already taken");
        }


        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());

        String encodedPassword = passwordEncoder.encode(userRegistrationRequest.password());
        user.setPassword(encodedPassword);

        user.setRole(Role.ROLE_USER);

        userDAO.insertUser(user);

        NotificationRequest notificationRequest = new NotificationRequest(
                user.getUserId(),
                user.getEmail(),
                String.format("Hi %s, welcome to Levantos...", user.getFirstName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

        log.info("User registered with ID: {}", user.getUserId());
    }


    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userDAO.selectUserById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with [%s] not found"
                                .formatted(userId)));

        boolean changes = false;

        if (userUpdateRequest.firstName() != null && !userUpdateRequest.firstName().equals(user.getFirstName())) {
            user.setFirstName(userUpdateRequest.firstName());
            changes = true;
        }
        if (userUpdateRequest.lastName() != null && !userUpdateRequest.lastName().equals(user.getLastName())) {
            user.setLastName(userUpdateRequest.lastName());
            changes = true;
        }
        if (userUpdateRequest.email() != null && !userUpdateRequest.email().equals(user.getEmail())) {
            if (userDAO.existPersonWithEmail(userUpdateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            user.setEmail(userUpdateRequest.email());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("No changes detected");
        }
        userDAO.updateUser(user);

        NotificationRequest notificationRequest = new NotificationRequest(

                user.getUserId(),
                user.getEmail(),
                String.format("Hi %s, User was updated", user.getFirstName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    public void deleteUserById(Long userId) {
        if (!userDAO.existUserWithId(userId)) {
            throw new ResourceNotFoundException(
                    "User with id [%s] not found".
                            formatted(userId));
        }
        userDAO.deleteUserById(userId);
    }
}