package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.cinema.cinema.CinemaDTO;
import com.movie.client.notification.NotificationRequest;
import com.movie.users.roles.Role;

import com.movie.users.roles.RoleDAO;
import com.movie.users.users.exception.ResourceNotFoundException;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final RoleDAO roleDAO;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    private final PasswordEncoder passwordEncoder;






    public UserService(@Qualifier("userJdbc") UserDAO userDAO, UserDTOMapper userDTOMapper,
                       RoleDAO roleDAO, RabbitMqMessageProducer rabbitMqMessageProducer, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;

        this.roleDAO = roleDAO;

        this.rabbitMqMessageProducer = rabbitMqMessageProducer;


        this.passwordEncoder = passwordEncoder;
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
                                "User with id [%s] not found".
                                        formatted(id)));
    }

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        Role role = roleDAO.selectRoleById(userRegistrationRequest.roleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());

        // Ensure the password is encoded and set
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
}