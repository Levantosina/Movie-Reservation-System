package com.movie.movie.movie;

import com.movie.users.users.User;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author DMITRII LEVKIN on 27/11/2024
 * @project Movie-Reservation-System
 */
class MovieRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        MovieRowMapper movieRowMapper = new MovieRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("movie_id")).thenReturn(4L);
        when(resultSet.getString("movie_name")).thenReturn("AvatarTest");
        when(resultSet.getInt("year")).thenReturn(2025);
        when(resultSet.getString("country")).thenReturn("USA");
        when(resultSet.getString("genre")).thenReturn("Action");
        when(resultSet.getString("description")).thenReturn("Test");


        Movie actual = movieRowMapper.mapRow(resultSet,1);
        Movie expectedMovie = new Movie(4L,"AvatarTest",2025,"USA","Action","Test");
        assertThat(actual).isEqualTo(expectedMovie);
    }
}
