package com.movie.ticket;


import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author DMITRII LEVKIN on 20/01/2025
 * @project Movie-Reservation-System
 */
@Testcontainers
public class TestContainerTest extends TicketAbstractDaoUnitTest {

    @Test
    void canStartPostgresDB(){
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}