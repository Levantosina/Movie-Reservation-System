package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;


import com.movie.client.notification.NotificationRequest;
import com.movie.common.UserDTO;
import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author DMITRII LEVKIN on 15/10/2024
 * @project MovieReservationSystem
 */
@Service
public class AdminService {

    private final UserDAO userDAO;


    private final PasswordEncoder passwordEncoder;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    private final UserDTOMapper userDTOMapper;

    public AdminService(UserDAO userDAO, PasswordEncoder passwordEncoder, RabbitMqMessageProducer rabbitMqMessageProducer, UserDTOMapper userDTOMapper) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.userDTOMapper = userDTOMapper;
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

    public void registerAdministrator(AdminRegistrationRequest adminRegistrationRequest, User currentAdmin) {


        if (!currentAdmin.getRole().equals(Role.ROLE_ADMIN)) {
            throw new RuntimeException("Only admins");
        }

        Optional<User> existingUser = userDAO.selectUserByEmail(adminRegistrationRequest.email());
        if (existingUser.isPresent()) {
            throw new DuplicateResourceException("Email is already taken");
        }


        User admin = new User();
        admin.setFirstName(adminRegistrationRequest.firstName());
        admin.setLastName(adminRegistrationRequest.lastName());
        admin.setEmail(adminRegistrationRequest.email());

        String encodedPassword = passwordEncoder.encode(adminRegistrationRequest.password());
        admin.setPassword(encodedPassword);
        admin.setRole(Role.ROLE_ADMIN);

        userDAO.insertUser(admin);

        NotificationRequest notificationRequest = new NotificationRequest(
                admin.getUserId(),
                admin.getEmail(),
                String.format("Hi %s, welcome to Admin...", admin.getFirstName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    public void updateUserRoles(String email, String newRole) {
        Optional<User> userOpt = userDAO.selectUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("ROLE_ADMIN".equals(newRole)) {
                user.setRole(Role.ROLE_ADMIN);
            } else if ("ROLE_USER".equals(newRole)) {
                user.setRole(Role.ROLE_USER);
            } else {
                throw new IllegalArgumentException("Invalid role: " + newRole);
            }
            userDAO.updateUser(user);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        NotificationRequest notificationRequest = new NotificationRequest(

                userOpt.get().getUserId(),
                userOpt.get().getEmail(),
                String.format("Hi %s, Admin was updated", userOpt.get().getFirstName())
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }


    public void resetAdminPassword(String userName, String newPlainPassword) {

        User admin = userDAO.selectUserByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        String hashedPassword = passwordEncoder.encode(newPlainPassword);
        System.out.println("Hashed password: " + hashedPassword);
        admin.setPassword(hashedPassword);
        userDAO.updateUser(admin);

        NotificationRequest notificationRequest = new NotificationRequest(
                admin.getUserId(),
                admin.getEmail(),
                String.format("Your password has been successfully changed...", admin.getFirstName())
        );
        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    public UserDTO getAdminById(Long id) {
        Optional<User> user = userDAO.getAdminById(id);
        if (user.isPresent()) {
            return userDTOMapper.apply(user.get());
        } else {
            throw new ResourceNotFoundException("Admin not found with id " + id);
        }
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

}

