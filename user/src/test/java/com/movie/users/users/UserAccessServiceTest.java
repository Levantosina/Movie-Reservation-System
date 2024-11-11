package com.movie.users.users;

import com.movie.users.AbstractDaoUnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * @author DMITRII LEVKIN on 11/11/2024
 * @project Movie-Reservation-System
 */
class UserAccessServiceTest  extends AbstractDaoUnitTest {

    private UserAccessService underTestUser;

    private final UserRowMapper userRowMapper =new UserRowMapper();


    @BeforeEach
    void setUp() {
        underTestUser=new UserAccessService(
                getJdbcTemplate(),
                userRowMapper
        );
    }

    @Test
    void selectAllUsers() {
//        Role userRole = Role.builder()
//                .roleName("USER")
//                .description("user")
//                .build();

//        underTestRole.insert(userRole)
//        User user= new User(
//                FAKER.name().firstName(),
//                FAKER.name().lastName(),
//                FAKER.internet().safeEmailAddress()+"-"+UUID.randomUUID(),
//                "password",
//                Set.of(userRole));




//        underTest.insertUser(user);
//        List<User> actual=underTest.selectAllUsers();
//        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectUserById() {
    }

    @Test
    void selectUserByEmail() {
    }

    @Test
    void existPersonWithEmail() {
    }

    @Test
    void existUserWithId() {
    }

    @Test
    void insertUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUserById() {
    }
}