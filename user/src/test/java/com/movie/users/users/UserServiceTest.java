package com.movie.users.users;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;
import com.movie.common.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author DMITRII LEVKIN on 12/11/2024
 * @project Movie-Reservation-System
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService underTest;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDAO userDAO;

    private final UserDTOMapper userDTOMapper = new UserDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer ;

    @BeforeEach
    void setUp() {

        underTest= new UserService(userDAO,userDTOMapper,rabbitMqMessageProducer,passwordEncoder);
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
        underTest.getAllUsers();
        verify(userDAO).selectAllUsers();
    }

    @Test
    void getUserById() {
        long id=1;
        User user= new User(id,"Abra","kadabra","abrakadabra.test.com","password",Role.ROLE_USER);
        when(userDAO.selectUserById((Long) id)).thenReturn(Optional.of(user));

        UserDTO expected = userDTOMapper.apply(user);
        UserDTO actual= underTest.getUserById(id);
        assertThat(actual).isEqualTo(expected);
        verify(userDAO).selectUserById(id);
    }

    @Test
    void registerUser() {

        String email = "test@test.com";
        when(userDAO.selectUserByEmail(email)).thenReturn(Optional.empty());

        UserRegistrationRequest request = new UserRegistrationRequest(
                "Abra",
                "Kadabra",
                email,
                "password",
                "ROLE_USER"
        );

        String passwordHash = "2331234355";
        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);


        underTest.registerUser(request);


        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).insertUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();


        assertThat(capturedUser.getUserId()).isNull();
        assertThat(capturedUser.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedUser.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(passwordHash);
        assertThat(capturedUser.getRole()).isEqualTo(Role.ROLE_USER);


        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
        NotificationRequest capturedNotification = notificationCaptor.getValue();
        assertThat(capturedNotification.toUserEmail()).isEqualTo(email);
        assertThat(capturedNotification.message()).contains("Welcome to Levantos");
    }

    @Test
    void deleteUserById() {
        long id= 1;
        when(userDAO.existUserWithId(id)).thenReturn(true);
        underTest.deleteUserById(id);
        verify(userDAO).deleteUserById(id);
    }
}