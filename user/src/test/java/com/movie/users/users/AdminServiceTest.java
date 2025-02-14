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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


/**
 * @author DMITRII LEVKIN on 13/11/2024
 * @project Movie-Reservation-System
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private AdminService underTest;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private  UserDAO userDAO;
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer;
    @Mock
    private UserDTOMapper userDTOMapper;


    @BeforeEach
    void setUp() {

        underTest=new AdminService(userDAO,passwordEncoder,rabbitMqMessageProducer,userDTOMapper);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void getAdminById() {
        long id = 2;
        User admin = new User(id, "Admin", "User", "admin@test.com", "password", Role.ROLE_ADMIN);

        when(userDAO.getAdminById(id)).thenReturn(Optional.of(admin));
        when(userDTOMapper.apply(admin)).thenReturn(new UserDTO(id, "Admin", "User", "admin@test.com","ROLE_ADMIN"));

        UserDTO expected = new UserDTO(id, "Admin", "User", "admin@test.com","ROLE_ADMIN");
        UserDTO actual = underTest.getAdminById(id);

        assertThat(actual).isEqualTo(expected);
        verify(userDAO).getAdminById(id);
    }

    @Test
    void registerAdministrator() {

        String email = "test@test.com";
        when(userDAO.selectUserByEmail(email)).thenReturn(Optional.empty());

        User admin = new User(1L,"Avada","kedavra","avadakedavra.test.com","password",Role.ROLE_ADMIN);

        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Abra",
                "Kadabra",
                email,
                "password"

        );

        String passwordHash = "2331234355";
        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);


        underTest.registerAdministrator(request,admin);


        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).insertUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();


        assertThat(capturedUser.getUserId()).isNull();
        assertThat(capturedUser.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedUser.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getPassword()).isEqualTo(passwordHash);
        assertThat(capturedUser.getRole()).isEqualTo(Role.ROLE_ADMIN);


        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
        NotificationRequest capturedNotification = notificationCaptor.getValue();
        assertThat(capturedNotification.toUserEmail()).isEqualTo(email);
        assertThat(capturedNotification.message()).contains("Hi Abra, welcome to Admin...");   }


    @Test
    void updateUserRoles() {
        String username = "test@example.com";
        User user = new User(1L, "Avada", "kedavra", username, "password", Role.ROLE_ADMIN);
        when(userDAO.selectUserByEmail(username)).thenReturn(Optional.of(user));
        underTest.updateUserRoles(username, "ROLE_USER");
        assertEquals(Role.ROLE_USER, user.getRole(), "Role should be updated to ROLE_USER");
        verify(userDAO, times(1)).updateUser(user);
    }

    @Test
    void resetAdminPassword() {
        long id =1;
        String email= "avadakedavra.test.com";
        String firstName ="Avada";
        String lastName ="kedavra";
        String password= "password";
        User admin = new User(id,firstName,lastName,email,password,Role.ROLE_ADMIN);
        when(userDAO.selectUserByEmail(email)).thenReturn(Optional.of(admin));
        String newPlainPassword ="expelliarmus";

        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest (firstName,lastName,email,newPlainPassword);

        when(passwordEncoder.encode(adminRegistrationRequest.password())).thenReturn(newPlainPassword);
        underTest.resetAdminPassword(email,newPlainPassword);

        ArgumentCaptor<User>userArgumentCaptor=
                ArgumentCaptor.forClass(User.class);
        verify(userDAO).updateUser(userArgumentCaptor.capture());

        User capturedUser= userArgumentCaptor.getValue();

        assertThat(capturedUser.getFirstName()).isEqualTo(adminRegistrationRequest.firstName());
        assertThat(capturedUser.getLastName()).isEqualTo(adminRegistrationRequest.lastName());
        assertThat(capturedUser.getEmail()).isEqualTo(adminRegistrationRequest.email());
        assertThat(capturedUser.getPassword()).isEqualTo(adminRegistrationRequest.password());
        assertThat(capturedUser.getRole()).isEqualTo(Role.ROLE_ADMIN);


        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
        NotificationRequest capturedNotification = notificationCaptor.getValue();
        assertThat(capturedNotification.toUserEmail()).isEqualTo(email);
        assertThat(capturedNotification.message()).contains("Your password has been successfully changed...");   }

}