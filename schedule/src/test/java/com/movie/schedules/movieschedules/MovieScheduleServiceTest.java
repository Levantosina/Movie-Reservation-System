package com.movie.schedules.movieschedules;


import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.cinemaClient.CinemaClient;
import com.movie.client.movieClient.MovieClient;
import com.movie.client.seatClient.SeatClient;
import com.movie.common.TotalSeatsDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author DMITRII LEVKIN on 19/12/2024
 * @project Movie-Reservation-System
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class MovieScheduleServiceTest {
    @Mock
    private  MovieScheduleService underTest;
    @Mock
    private MovieScheduleDAO scheduleDAO;
    @Mock
    private SeatClient seatClient;
    @Mock
    private  MovieClient movieClient;
    @Mock
    private CinemaClient cinemaClient;
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer;



    private final MovieScheduleDTOMapper scheduleDTOMapper = new MovieScheduleDTOMapper();
    private final Long scheduleId = 35L;
    private final LocalDate date = LocalDate.parse("2024-12-03");
    private final LocalTime startTime = LocalTime.parse("10:00:00");
    private final LocalTime endTime = LocalTime.parse("12:00:00");
    private final  int availableSeats = 2;
    private final Long cinemaId = 1L;
    private final  Long movieId = 1L;




    @BeforeEach
    void setUp() {
        underTest = new MovieScheduleService(scheduleDAO,scheduleDTOMapper,seatClient,movieClient,cinemaClient,rabbitMqMessageProducer );
    }

    @Test
    void getAllSchedules() {
        underTest.getAllSchedules();
        verify(scheduleDAO).selectAllSchedules();
    }

    @Test
    void createSchedule() {

        TotalSeatsDTO mockTotalSeatsDTO = new TotalSeatsDTO(10);
        when(seatClient.getTotalSeatsByCinemaId(cinemaId)).thenReturn(mockTotalSeatsDTO);
        when(cinemaClient.existsById(cinemaId)).thenReturn(true);
        when(movieClient.existsById(movieId)).thenReturn(true);

        MovieScheduleRegistrationRequest registrationRequest = new MovieScheduleRegistrationRequest(
                date, startTime, endTime, cinemaId, movieId
        );
        underTest.createSchedule(registrationRequest);

        ArgumentCaptor<MovieSchedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(MovieSchedule.class);
        verify(scheduleDAO).createSchedule(scheduleArgumentCaptor.capture());


        MovieSchedule capturedSchedule = scheduleArgumentCaptor.getValue();

        assertThat(capturedSchedule.getDate()).isEqualTo(registrationRequest.date());
        assertThat(capturedSchedule.getStartTime()).isEqualTo(registrationRequest.startTime());
        assertThat(capturedSchedule.getEndTime()).isEqualTo(registrationRequest.endTime());
        assertThat(capturedSchedule.getAvailableSeats()).isEqualTo(mockTotalSeatsDTO.totalSeats());  // Verify against mocked value
        assertThat(capturedSchedule.getCinemaId()).isEqualTo(registrationRequest.cinemaId());
        assertThat(capturedSchedule.getMovieId()).isEqualTo(registrationRequest.movieId());
    }

    @Test
    void findByCinemaId() {
        MovieSchedule movieSchedule = new MovieSchedule(
                scheduleId, date,startTime,endTime,availableSeats,cinemaId,movieId
        );

        when(scheduleDAO.selectSchedulesByCinemaId(cinemaId)).thenReturn(List.of(movieSchedule));

        MovieScheduleDTO expected = scheduleDTOMapper.apply(movieSchedule);
        List<MovieScheduleDTO> actual = underTest.findByCinemaId(cinemaId);

        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst()).isEqualTo(expected);

        verify(scheduleDAO).selectSchedulesByCinemaId(cinemaId);

    }

    @Test
    void findByMovieId() {

        MovieSchedule movieSchedule = new MovieSchedule(
                scheduleId, date,startTime,endTime,availableSeats,cinemaId,movieId
        );

        when(scheduleDAO.selectSchedulesByMovieId(movieId)).thenReturn(List.of(movieSchedule));

        MovieScheduleDTO expected = scheduleDTOMapper.apply(movieSchedule);
        List<MovieScheduleDTO> actual = underTest.findByMovieId(movieId);

        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst()).isEqualTo(expected);

        verify(scheduleDAO).selectSchedulesByMovieId(movieId);

    }


    @Test
    void findByDate() {

        MovieSchedule movieSchedule = new MovieSchedule(
                scheduleId, date,startTime,endTime,availableSeats,cinemaId,movieId
        );

        when(scheduleDAO.findByDate(date)).thenReturn(List.of(movieSchedule));

        MovieScheduleDTO expected = scheduleDTOMapper.apply(movieSchedule);
        List<MovieScheduleDTO> actual = underTest.findByDate(date);

        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst()).isEqualTo(expected);

        verify(scheduleDAO).findByDate(date);
    }

    @Test
    void findByCinemaIdAndMovieId() {

        MovieSchedule movieSchedule = new MovieSchedule(
                scheduleId, date,startTime,endTime,availableSeats,cinemaId,movieId
        );

        when(scheduleDAO.findByCinemaIdAndMovieId(movieId,cinemaId)).thenReturn(List.of(movieSchedule));

        MovieScheduleDTO expected = scheduleDTOMapper.apply(movieSchedule);
        List<MovieScheduleDTO> actual = underTest.findByCinemaIdAndMovieId(movieId,cinemaId);

        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst()).isEqualTo(expected);

        verify(scheduleDAO).findByCinemaIdAndMovieId(movieId,cinemaId);
    }

    @Test
    void updateSchedule() {
        MovieSchedule movieSchedule = new MovieSchedule(
                scheduleId, date,startTime,endTime,availableSeats,cinemaId,movieId
        );
        when(scheduleDAO.selectScheduleById(scheduleId)).thenReturn(Optional.of(movieSchedule));

         LocalDate uDate = LocalDate.parse("2024-12-05");
         LocalTime uStartTime = LocalTime.parse("13:00:00");
         LocalTime uEndTime = LocalTime.parse("15:00:00");
         int uAvailableSeats = 12;
         long uCinemaId = 3;
         long uMovieId = 3;

        ScheduleUpdateRequest scheduleUpdateRequest = new ScheduleUpdateRequest(
                uDate,uStartTime,uEndTime,uAvailableSeats,uCinemaId,uMovieId
        );
         underTest.updateSchedule(scheduleId,scheduleUpdateRequest);

        ArgumentCaptor<MovieSchedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(MovieSchedule.class);
        verify(scheduleDAO).upDateSchedule(scheduleArgumentCaptor.capture());
        MovieSchedule updatedSchedule = scheduleArgumentCaptor.getValue();


        assertThat(updatedSchedule.getScheduleId()).isEqualTo(scheduleId);
        assertThat(updatedSchedule.getDate()).isEqualTo(scheduleUpdateRequest.date());
        assertThat(updatedSchedule.getStartTime()).isEqualTo(scheduleUpdateRequest.startTime());
        assertThat(updatedSchedule.getEndTime()).isEqualTo(scheduleUpdateRequest.endTime());
        assertThat(updatedSchedule.getAvailableSeats()).isEqualTo(scheduleUpdateRequest.availableSeats());
        assertThat(updatedSchedule.getCinemaId()).isEqualTo(scheduleUpdateRequest.cinemaId());
        assertThat(updatedSchedule.getMovieId()).isEqualTo(scheduleUpdateRequest.movieId());

    }

    @Test
    void deleteSchedule() {
        underTest.deleteSchedule(scheduleId);
        verify(scheduleDAO).deleteSchedule(scheduleId);
    }
}

