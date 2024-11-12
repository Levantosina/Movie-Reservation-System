package com.movie.users.users;

import com.movie.users.AbstractDaoUnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author DMITRII LEVKIN on 11/11/2024
 * @project Movie-Reservation-System
 */
class UserAccessServiceTest  extends AbstractDaoUnitTest {

    private UserAccessService underTestUser;

    private final UserRowMapper userRowMapper = new UserRowMapper();


    @BeforeEach
    void setUp() {
        underTestUser = new UserAccessService(
                getJdbcTemplate(),
                userRowMapper
        );
    }

    @Test
    void selectAllUsers() {
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID())
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        underTestUser.insertUser(user);
        List<User> actual = underTestUser.selectAllUsers();
        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectUserById() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);

        long userId = underTestUser.selectAllUsers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();


        Optional<User> actual = underTestUser.selectUserById((long) userId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getUserId()).isEqualTo(userId);
            assertThat(c.getFirstName()).isEqualTo(user.getFirstName());
            assertThat(c.getLastName()).isEqualTo(user.getLastName());
            assertThat(c.getEmail()).isEqualTo(user.getEmail());
            assertThat(c.getRole()).isEqualTo(user.getRole());

        });

    }

    @Test
    void selectUserByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);

        String emailTest = underTestUser.selectAllUsers()
                .stream()
                .map(User::getEmail)
                .filter(cEmail -> cEmail.equals(email))
                .findFirst()
                .orElseThrow();


        Optional<User> actual = underTestUser.selectUserByEmail((String) emailTest);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {

            assertThat(c.getFirstName()).isEqualTo(user.getFirstName());
            assertThat(c.getLastName()).isEqualTo(user.getLastName());
            assertThat(c.getEmail()).isEqualTo(user.getEmail());
            assertThat(c.getRole()).isEqualTo(user.getRole());

        });


    }

    @Test
    void existPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);

        boolean actual = underTestUser.existPersonWithEmail(email);
        assertThat(actual).isTrue();

    }

    @Test
    void existUserWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);
        boolean actual = underTestUser.existUserWithId(user.getUserId());
        assertThat(actual).isTrue();

    }

    @Test
    void insertUser() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        underTestUser.insertUser(user);

        Optional<User> actual = underTestUser.selectUserByEmail(email);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getFirstName()).isEqualTo(user.getFirstName());
            assertThat(c.getLastName()).isEqualTo(user.getLastName());
            assertThat(c.getEmail()).isEqualTo(user.getEmail());
            assertThat(c.getPassword()).isEqualTo(user.getPassword());
            assertThat(c.getRole()).isEqualTo(user.getRole());
        });
    }


    @Test
    void updateUser() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);

        long userId = underTestUser.selectAllUsers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();


        String updatedFirstName = "UpdatedFirstName";
        String updatedLastName = "UpdatedLastName";
        String updatedPassword = "UpdatedPassword";

        user.setFirstName(updatedFirstName);
        user.setLastName(updatedLastName);
        user.setPassword(updatedPassword);

        underTestUser.updateUser(user);


        Optional<User> updatedUser = underTestUser.selectUserById(userId);
        assertThat(updatedUser).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getFirstName()).isEqualTo(updatedFirstName);
            assertThat(u.getLastName()).isEqualTo(updatedLastName);
            assertThat(u.getPassword()).isEqualTo(updatedPassword);
        });
    }

    @Test
    void deleteUserById() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        User user = User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(email)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        underTestUser.insertUser(user);


        long userId = underTestUser.selectAllUsers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();


        underTestUser.deleteUserById(userId);


        Optional<User> deletedUser = underTestUser.selectUserById(userId);
        assertThat(deletedUser).isNotPresent();
    }
}