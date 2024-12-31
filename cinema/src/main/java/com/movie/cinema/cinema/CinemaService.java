package com.movie.cinema.cinema;

import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.notification.NotificationRequest;
import com.movie.client.seatClient.SeatClient;
import com.movie.exceptions.DuplicateResourceException;
import com.movie.exceptions.ResourceNotFoundException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DMITRII LEVKIN on 24/09/2024
 * @project MovieReservationSystem
 */
@Service
public class CinemaService {

    private  final CinemaDAO cinemaDAO;
    private final CinemaDTOMapper cinemaDTOMapper;
    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    private final SeatClient seatClient;

    @Getter
    private Map<String, Long> cinemaMap = new HashMap<>();


    public CinemaService(@Qualifier("cinemaJdbc") CinemaDAO cinemaDAO, CinemaDTOMapper cinemaDTOMapper, RabbitMqMessageProducer rabbitMqMessageProducer, SeatClient seatClient) {
        this.cinemaDAO = cinemaDAO;
        this.cinemaDTOMapper = cinemaDTOMapper;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.seatClient = seatClient;
    }
    public List<CinemaDTO> getAllCinemas() {
        return cinemaDAO.selectAllCinemas()
                .stream()
                .map(cinemaDTOMapper)
                .collect(Collectors.toList());
    }
    public CinemaDTO getCinemaById(Long id) {
        return cinemaDAO.selectCinemaById(id)
                .map(cinemaDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cinema with id [%s] not found".
                                        formatted(id)));
    }

    public CinemaDTO getCinemaByName(String cinemaName) {
        return cinemaDAO.selectCinemaByCinemaName(cinemaName)
                .map(cinemaDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cinema with name [%s] not found".
                                        formatted(cinemaName)));
    }
    public Long getCinemaIdByName(String cinemaName) {
        Optional<Cinema> cinemaOpt = cinemaDAO.selectCinemaByCinemaName(cinemaName);
        return cinemaOpt.map(Cinema::getCinemaId).orElse(null);
    }

    public void registerCinema(CinemaRegistrationRequest cinemaRegistrationRequest){

        String cinemaName = cinemaRegistrationRequest.cinemaName();

        if(cinemaDAO.selectCinemaByCinemaName(cinemaName).isPresent()){
            throw  new DuplicateResourceException("Cinema name must be unique.");
        }

        Cinema cinema = new Cinema();
        cinema.setCinemaName(cinemaRegistrationRequest.cinemaName());
        cinema.setCinemaLocation(cinemaRegistrationRequest.cinemaLocation());

        cinemaDAO.insertCinema(cinema);

        NotificationRequest notificationRequest = new NotificationRequest(
                cinema.getCinemaId(), "Cinema created", "The cinema " + cinema.getCinemaName() + " has been created."
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }
    @PostConstruct
    public void loadCinemasFromCSV() {
        String csvFilePath = "cinemas.csv";
        cinemaMap = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(csvFilePath))))) {
            String[] values;


            csvReader.readNext();

            while ((values = csvReader.readNext()) != null) {
                String cinemaName = values[0];
                String cinemaLocation = values[1];
                Cinema cinema = new Cinema();
                cinema.setCinemaName(cinemaName);
                cinema.setCinemaLocation(cinemaLocation);
                cinemaDAO.insertCinema(cinema);

                cinemaMap.put(cinemaName, cinema.getCinemaId());      }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}