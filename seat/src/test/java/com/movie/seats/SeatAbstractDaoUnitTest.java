package com.movie.seats;
import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@Testcontainers
public abstract class SeatAbstractDaoUnitTest {
    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure()
                        .locations("classpath:db/migration/seat")
                        .dataSource(
                        postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(),
                        postgreSQLContainer.getPassword())
                .load();
        flyway.migrate();
        System.out.println();

    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("seat")
                    .withUsername("levantos")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
        registry.add(
                "spring.datasource.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainer::getPassword
        );

    }

    private static DataSource getDataSource(){
        return   DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword()).build();

    }
    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected static  final Faker FAKER=new Faker();


}


