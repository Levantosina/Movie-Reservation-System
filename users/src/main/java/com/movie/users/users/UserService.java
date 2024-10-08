package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.cinema.cinema.CinemaDTO;
import com.movie.client.notification.NotificationRequest;
import com.movie.users.roles.Role;

import com.movie.users.roles.RoleDAO;
import com.movie.users.users.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    private final Map<String, Long> cinemaMap = new HashMap<>();


    public UserService(@Qualifier("jdbc") UserDAO userDAO, UserDTOMapper userDTOMapper,
                       RoleDAO roleDAO, RabbitMqMessageProducer rabbitMqMessageProducer) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;

        this.roleDAO = roleDAO;

        this.rabbitMqMessageProducer = rabbitMqMessageProducer;

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

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        Role role = roleDAO.selectRoleById(userRegistrationRequest.roleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setEmail(userRegistrationRequest.email());
        user.setRole(role);

        Long generatedUserId = userDAO.insertUser(user);
        user.setUserId(generatedUserId);

        System.out.println(user);

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

