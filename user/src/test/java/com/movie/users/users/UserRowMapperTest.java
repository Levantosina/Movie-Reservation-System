package com.movie.users.users;

import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author DMITRII LEVKIN on 12/11/2024
 * @project Movie-Reservation-System
 */
class UserRowMapperTest {

    @Test
    void mapRow() throws SQLException {

      UserRowMapper userRowMapper = new UserRowMapper();

      ResultSet resultSet = mock(ResultSet.class);

      when(resultSet.getLong("user_id")).thenReturn(2L);
      when(resultSet.getString("first_name")).thenReturn("Abra");
        when(resultSet.getString("last_name")).thenReturn("Kadabrra");
        when(resultSet.getString("email")).thenReturn("abrakadabra@test.com");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role_name")).thenReturn("ROLE_USER");

        User actual=  userRowMapper.mapRow(resultSet,1);
             User expectedUser = new User(
                 2L,"Abra","Kadabrra",
                     "abrakadabra@test.com","password",Role.ROLE_USER);
             assertThat(actual).isEqualTo(expectedUser);
    }

}