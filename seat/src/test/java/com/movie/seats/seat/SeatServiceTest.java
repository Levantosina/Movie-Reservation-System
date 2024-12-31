package com.movie.seats.seat;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.cinemaClient.CinemaClient;
import com.movie.client.notification.NotificationRequest;
import com.movie.common.CinemaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author DMITRII LEVKIN on 16/12/2024
 * @project Movie-Reservation-System
 */
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    private  SeatService underTest;
    @Mock
    private SeatDAO seatDAO;
    @Mock
    private CinemaClient cinemaClient;
    @InjectMocks
    private SeatService seatService;
    private final  SeatDTOMapper seatDTOMapper = new SeatDTOMapper();
    @Mock
    private RabbitMqMessageProducer rabbitMqMessageProducer;

    @BeforeEach
    void setUp() {
        underTest = new SeatService(seatDAO,seatDTOMapper,cinemaClient,rabbitMqMessageProducer);
    }

    @Test
    void getAllSeats() {
        underTest.getAllSeats();
        verify(seatDAO).selectAllSeats();
    }

    @Test
    void getSeat() {
        long seatId = 14;
        Seat seat = new Seat(seatId,12,"A","VIP",1L,false);
        when(seatDAO.selectSeatById(seatId)).thenReturn(Optional.of(seat));

        SeatDTO expected  = seatDTOMapper.apply(seat);
        SeatDTO actual = underTest.getSeat(seatId);
        assertThat(actual).isEqualTo(expected);
        verify(seatDAO).selectSeatById(seatId);
    }

    @Test
    void registerNewSeat() {
        SeatRegistrationRequest seatRegistrationRequest = new SeatRegistrationRequest(
                2,
                "A",
                "VIP",
                2L,
                false
        );

        underTest.registerNewSeat(seatRegistrationRequest);

        ArgumentCaptor<Seat> seatArgumentCaptor = ArgumentCaptor.forClass(Seat.class);
        verify(seatDAO).insertSeat(seatArgumentCaptor.capture());
        Seat capturedSeat = seatArgumentCaptor.getValue();


        assertThat(capturedSeat.getCinemaId()).isEqualTo(seatRegistrationRequest.cinemaId());
        assertThat(capturedSeat.getSeatNumber()).isEqualTo(seatRegistrationRequest.seatNumber());
        assertThat(capturedSeat.getRow()).isEqualTo(seatRegistrationRequest.row());
        assertThat(capturedSeat.getType()).isEqualTo(seatRegistrationRequest.type());
        assertThat(capturedSeat.isOccupied()).isEqualTo(seatRegistrationRequest.isOccupied());

        ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(rabbitMqMessageProducer).publish(
                notificationCaptor.capture(),
                eq("internal.exchange"),
                eq("internal.notification.routing-key")
        );
    }

    @Test
    void getSeatsByCinema() {
        long cinemaId = 1;
        CinemaDTO cinemaDTO = new CinemaDTO(cinemaId,"Amax","NU");
        Seat seat = new Seat(1L, 2, "A", "VIP", 1L, false);
        when(seatDAO.selectSeatsByCinemaId(cinemaDTO.cinemaId())).thenReturn(List.of(seat));

        SeatDTO expected = seatDTOMapper.apply(seat);
        List<SeatDTO> actual = underTest.getSeatsByCinema(cinemaDTO);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expected);
        verify(seatDAO).selectSeatsByCinemaId(cinemaDTO.cinemaId());
    }




//    @ParameterizedTest(name = "cinema name ''{0}'' should return id {1}")
//    @CsvSource({
//            "Amazonia, 1",
//            "Atmosphere, 2",
//            "Stars, 3"
//    })
   // void testGetCinemaId(String cinemaName, Long expectedCinemaId) {

//        when(cinemaClient.getCinemaIdByName(cinemaName)).thenReturn(expectedCinemaId);
//        Long cinemaId = seatService.getCinemaId(cinemaName);
//        assertThat(cinemaId).isEqualTo(expectedCinemaId);
//        verify(cinemaClient, times(1)).getCinemaIdByName(cinemaName);
//    }
//    void testGetCinemaId(String cinemaName, Long expectedCinemaId,String cinemaLocation) {
//
//        CinemaDTO mockedCinemaDTO = new CinemaDTO(expectedCinemaId, cinemaName,cinemaLocation);
//
//
//        when(cinemaClient.getCinemaIdByName(cinemaName)).thenReturn(mockedCinemaDTO.cinemaId());
//
//
//        CinemaDTO cinemaDTO = seatService.getCinemaId(mockedCinemaDTO.cinemaName());
//
//
//        assertThat(cinemaDTO).isEqualTo(mockedCinemaDTO);
//
//
//        verify(cinemaClient, times(1)).getCinemaIdByName(cinemaName);
//    }


    @Test
    void getTotalSeatsByCinemaId() {
        Long cinemaId = 1L;
        int expected = 100;
        CinemaDTO cinemaDTO = new CinemaDTO(cinemaId,"Bla","NY");


        when(seatDAO.countSeatsByCinemaId(cinemaDTO.cinemaId())).thenReturn(expected);


        int actual = seatService.getTotalSeatsByCinemaId(cinemaDTO);


        assertThat(actual).isEqualTo(expected);


        verify(seatDAO, times(1)).countSeatsByCinemaId(cinemaDTO.cinemaId());
    }
}