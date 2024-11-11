package com.movie.users;


import com.movie.users.AbstractDaoUnitTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author DMITRII LEVKIN on 11/11/2024
 * @project Movie-Reservation-System
 */
@Testcontainers
public class TestContainerTest extends AbstractDaoUnitTest {

    @Test
    void canStartPostgresDB(){
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
