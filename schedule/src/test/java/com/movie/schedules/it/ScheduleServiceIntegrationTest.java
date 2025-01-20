package com.movie.schedules.it;


import com.movie.jwt.jwt.JWTUtil;
import com.movie.schedules.MovieScheduleApp;
import com.movie.schedules.ScheduleAbstractDaoUnitTest;
import com.movie.schedules.movieschedules.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author DMITRII LEVKIN on 22/12/2024
 * @project Movie-Reservation-System
 */
@SpringBootTest(classes = MovieScheduleApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j

public class ScheduleServiceIntegrationTest  extends ScheduleAbstractDaoUnitTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private MovieScheduleService scheduleService;
    @Autowired
    private MovieScheduleDAO movieScheduleDAO;
    @Autowired
    MovieScheduleDTOMapper scheduleDTOMapper;
    private String validToken;


    private static final String SCHEDULE_PATH = "/api/v1/schedules";

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.issueToken("username", Map.of("role", "ROLE_ADMIN"));
        log.info("validToken = : {}", validToken);
    }

    @Test
    void canCreateNewScheme() {
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");

        long cinemaId = 1;
        long movieId = 1;

        MovieScheduleRegistrationRequest registrationRequest = new MovieScheduleRegistrationRequest(
                date, startTime, endTime,  cinemaId, movieId
        );
        webTestClient.post()
                .uri(SCHEDULE_PATH)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .bodyValue(registrationRequest)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void canGetScheduleById() {
        long scheduleId = 3;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;
        MovieScheduleDTO scheduleDTO = new MovieScheduleDTO(scheduleId, date, startTime,
                endTime, availableSeats, cinemaId, movieId);
        when(scheduleService.getScheduleById(scheduleId)).thenReturn(scheduleDTO);
        webTestClient.get()
                .uri(SCHEDULE_PATH + "/" + scheduleId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MovieSchedule.class)
                .value(schedules -> {

                    assertThat(schedules.getScheduleId()).isEqualTo(scheduleId);
                });
    }

    @Test
    void canGetScheduleByMovieId() {
        long scheduleId = 19L;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;


        MovieScheduleDTO scheduleDTO = new MovieScheduleDTO(scheduleId, date, startTime, endTime, availableSeats, cinemaId, movieId);
        when(scheduleService.findByMovieId(movieId)).thenReturn(Collections.singletonList(scheduleDTO));


        webTestClient.get()
                .uri(SCHEDULE_PATH + "/movie/" + movieId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieSchedule.class)
                .value(schedules -> {
                    assertThat(schedules.size()).isGreaterThan(0);
                    assertThat(schedules.get(0).getScheduleId()).isEqualTo(scheduleId);
                    assertThat(schedules.get(0).getMovieId()).isEqualTo(movieId);
                });
    }

    @Test
    void canGetScheduleByCinemaId() {
        long scheduleId = 19L;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;


        MovieScheduleDTO scheduleDTO = new MovieScheduleDTO(scheduleId, date, startTime, endTime, availableSeats, cinemaId, movieId);
        when(scheduleService.findByCinemaId(cinemaId)).thenReturn(Collections.singletonList(scheduleDTO));


        webTestClient.get()
                .uri(SCHEDULE_PATH + "/cinema/" + cinemaId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieSchedule.class)
                .value(schedules -> {
                    assertThat(schedules.size()).isGreaterThan(0);
                    assertThat(schedules.get(0).getScheduleId()).isEqualTo(scheduleId);
                    assertThat(schedules.get(0).getCinemaId()).isEqualTo(cinemaId);
                });
    }

    @Test
    void canGetScheduleByMovieIdAndCinemaId() {
        long scheduleId = 19L;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;


        MovieScheduleDTO scheduleDTO = new MovieScheduleDTO(scheduleId, date, startTime, endTime, availableSeats, cinemaId, movieId);
        when(scheduleService.findByCinemaIdAndMovieId(cinemaId, movieId)).thenReturn(Collections.singletonList(scheduleDTO));


        webTestClient.get()
                .uri(SCHEDULE_PATH + "/cinema/" + cinemaId + "/movie/" + movieId)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieSchedule.class)
                .value(schedules -> {

                    assertThat(schedules.size()).isGreaterThan(0);
                    assertThat(schedules.get(0).getScheduleId()).isEqualTo(scheduleId);
                    assertThat(schedules.get(0).getCinemaId()).isEqualTo(cinemaId);
                });
    }

    @Test
    void canGetScheduleByDate() {
        long scheduleId = 19L;
        LocalDate date = LocalDate.parse("2024-12-03");
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        int availableSeats = 2;
        long cinemaId = 1;
        long movieId = 1;

        MovieScheduleDTO scheduleDTO = new MovieScheduleDTO(scheduleId, date, startTime, endTime, availableSeats, cinemaId, movieId);
        when(scheduleService.findByDate(date)).thenReturn(Collections.singletonList(scheduleDTO));

        webTestClient.get()
                .uri(SCHEDULE_PATH + "/date/" + date)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieSchedule.class)
                .value(schedules -> {
                    assertThat(schedules.size()).isGreaterThan(0);
                    assertThat(schedules.get(0).getScheduleId()).isEqualTo(scheduleId);
                    assertThat(schedules.get(0).getDate()).isEqualTo(date);
                });
    }


    @Test
    void canUpdateSchedule() {
        ScheduleUpdateRequest registrationRequest = new ScheduleUpdateRequest(
                LocalDate.parse("2024-12-03"),
                LocalTime.parse("10:00:00"),
                LocalTime.parse("12:00:00"),
                43, 1L, 1L
        );
        webTestClient.put()
                .uri(SCHEDULE_PATH + "/{scheduleId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + validToken)
                .body(Mono.just(registrationRequest), ScheduleUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);

        assertThat(validToken).isNotNull();
        log.info("Token used for the test: {}", validToken);
    }
}
