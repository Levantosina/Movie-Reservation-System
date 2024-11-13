package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;


import com.movie.client.notification.NotificationRequest;

import com.movie.users.users.exception.DuplicateResourceException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * @author DMITRII LEVKIN on 15/10/2024
 * @project MovieReservationSystem
 */
@Service
public class AdminService {

    private final UserDAO userDAO;


    private final PasswordEncoder passwordEncoder;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    public AdminService(UserDAO userDAO, PasswordEncoder passwordEncoder, RabbitMqMessageProducer rabbitMqMessageProducer) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
    }


    public void registerAdministrator(AdminRegistrationRequest adminRegistrationRequest,User currentAdmin){


        if(!currentAdmin.getRole().equals(Role.ROLE_ADMIN)){
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
}
