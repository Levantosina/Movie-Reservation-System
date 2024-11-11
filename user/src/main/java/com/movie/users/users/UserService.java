package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;


import com.movie.client.notification.NotificationRequest;
import com.movie.common.UserDTO;

import com.movie.users.users.exception.DuplicateResourceException;
import com.movie.users.users.exception.RequestValidationException;
import com.movie.users.users.exception.ResourceNotFoundException;
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

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {


        Optional<User> existingUser = userDAO.selectUserByEmail(userRegistrationRequest.email());
        if (existingUser.isPresent()) {
            throw new DuplicateResourceException("Email is already taken");
        }
        String  roleName = userRegistrationRequest.roleName();
        Role role;
        try {
            role = Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Only regular users can register.");
        }


        if (!"ROLE_USER".equals(roleName)) {
            throw new RuntimeException("Invalid role. Only regular users can register.");
        }

        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());

        String encodedPassword = passwordEncoder.encode(userRegistrationRequest.password());
        user.setPassword(encodedPassword);

        user.setRole(role);

        userDAO.insertUser(user);

        Optional<User> savedUser = userDAO.selectUserByEmail(user.getEmail());
        if (savedUser.isPresent()) {
            user.setUserId(savedUser.get().getUserId());
        }

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