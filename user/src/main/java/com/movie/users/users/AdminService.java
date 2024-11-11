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
    }

    public void updateUserRoles(String username, Set<String> roles) {


        if(roles.size()!=1){
            throw  new IllegalArgumentException("User can have one role");
        }

        User user = userDAO.selectUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String roleName = roles.iterator().next();

       Role role= Role.valueOf(roleName);
        user.setRole(role);

        userDAO.insertUser(user);
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
