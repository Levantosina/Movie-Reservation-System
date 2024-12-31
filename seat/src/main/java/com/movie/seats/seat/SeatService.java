package com.movie.seats.seat;






import com.movie.amqp.RabbitMqMessageProducer;
import com.movie.client.cinemaClient.CinemaClient;
import com.movie.client.notification.NotificationRequest;
import com.movie.common.CinemaDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.movie.exceptions.RequestValidationException;
import com.movie.exceptions.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.util.List;

/**
 * @author DMITRII LEVKIN on 26/09/2024
 * @project MovieReservationSystem
 */
@Service
@Slf4j
public class SeatService {

    private final SeatDAO seatDAO;
    private final SeatDTOMapper seatDTOMapper;
    private final Map<String, Long> cinemaMap = new HashMap<>();

    private final CinemaClient cinemaClient;

    private final RabbitMqMessageProducer rabbitMqMessageProducer;

    @Value("${seats.resources}")
    private Resource csvFilePath;




    public SeatService(@Qualifier("seatJdbc") SeatDAO seatDAO, SeatDTOMapper seatDTOMapper, CinemaClient cinemaClient, RabbitMqMessageProducer rabbitMqMessageProducer) {
        this.seatDAO = seatDAO;
        this.seatDTOMapper = seatDTOMapper;

        this.cinemaClient = cinemaClient;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
    }

    public List<SeatDTO> getAllSeats() {
        return seatDAO.selectAllSeats()
                .stream()
                .map(seatDTOMapper)
                .collect(Collectors.toList());
    }

    public SeatDTO getSeat(Long seatId) {
        return seatDAO.selectSeatById(seatId)
                .map(seatDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seat with id [%s] not found".formatted(seatId))
                );
    }

    public BigDecimal getSeatPrice(String seatType) {
        return SeatType.getPriceByType(seatType);
    }

    public BigDecimal getSeatPrice(Long seatId) {
        Optional<Seat> seatOptional = seatDAO.selectSeatById(seatId);
        if (seatOptional.isPresent()) {
            Seat seat = seatOptional.get();
            SeatType seatType = SeatType.valueOf(seat.getType().toUpperCase());
            return seatType.getPrice();
        } else {
            throw new ResourceNotFoundException("Seat not found with ID: " + seatId);
        }

}

    public void registerNewSeat(SeatRegistrationRequest seatRegistrationRequest) {

        Seat seat = new Seat();
        seat.setSeatNumber(seatRegistrationRequest.seatNumber());
        seat.setRow(seatRegistrationRequest.row());
        seat.setCinemaId(seatRegistrationRequest.cinemaId());
        seat.setOccupied(seatRegistrationRequest.isOccupied());

        SeatType seatType = SeatType.valueOf(seatRegistrationRequest.type().toUpperCase());
        seat.setType(seatType.name());

        seatDAO.insertSeat(seat);



        NotificationRequest notificationRequest = new NotificationRequest(
                seat.getCinemaId(), "New seats created", "The cinema has been created."
        );

        rabbitMqMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }

    public List<SeatDTO> getSeatsByCinema(CinemaDTO cinemaDTO) {
        Long cinemaId = cinemaDTO.cinemaId();
        return seatDAO.selectSeatsByCinemaId(cinemaId)
                .stream()
                .map(seatDTOMapper)
                .collect(Collectors.toList());
    }


    public void  updateSeat(Long seatId,SeatUpdateRequest seatUpdateRequest){
        Seat seat = seatDAO.selectSeatById(seatId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Seat with [%s] not found".formatted(seatId)));

        seat.setSeatId(seatId);
        boolean changes = false;


        if (seatUpdateRequest.seatNumber() != null) {
            if (!seat.getSeatNumber().equals(seatUpdateRequest.seatNumber())) {
                throw new IllegalArgumentException("Seat number cannot be changed.");
            }
        }

        if (seatUpdateRequest.row() != null) {
            if (!seat.getRow().equals(seatUpdateRequest.row())) {
                throw new IllegalArgumentException("Row cannot be changed.");
            }
        }

        if (seatUpdateRequest.type() != null && !seat.getType().equals(seatUpdateRequest.type())) {
            seat.setType(seatUpdateRequest.type());
            changes = true;
        }

        if (seatUpdateRequest.cinemaId() != null && !seat.getCinemaId().equals(seatUpdateRequest.cinemaId())) {
            seat.setCinemaId(seatUpdateRequest.cinemaId());
            changes = true;
        }
        if (seatUpdateRequest.isOccupied() != null && seat.isOccupied() != seatUpdateRequest.isOccupied()) {
            seat.setOccupied(seatUpdateRequest.isOccupied());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes detected");
        }
            seatDAO.updateSeat(seat);

    }

    @PostConstruct
    public void loadSeatsFromCSV() {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(csvFilePath.getInputStream()))) {
            String[] values;
            boolean headerSkipped = false;

            while ((values = csvReader.readNext()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String cinemaName = values[0];
                List<String> rows = parseRows(values[1]);
                JSONObject details = new JSONObject(values[2].replace("'", "\""));

                System.out.println("Processing cinema: " + cinemaName); // Log cinema name
                Long cinemaId = getCinema(cinemaName);
                if (cinemaId != null) {
                    for (String row : rows) {
                        String seatDetails = details.getString(row);
                        processSeats(row, seatDetails, cinemaId);
                    }
                } else {
                    System.out.println("No cinema Id found for " + cinemaName);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to load seats from CSV: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while loading seats: " + e.getMessage(), e);
        }
    }

    private List<String> parseRows(String rowsString) {
        return Arrays.asList(rowsString.replace("[", "").replace("]", "").replace("'", "").split(", "));
    }


    private void processSeats(String row, String seatDetails, Long cinemaId) {

        int seatCount = parseSeatCount(seatDetails); ////  6 seats to 6)


        int vipCount = extractSeatCount(seatDetails, "VIP");
        int disabledCount = extractSeatCount(seatDetails, "disabled");


        for (int i = 1; i <= seatCount; i++) {
            Seat seat = new Seat();
            seat.setRow(row);
            seat.setSeatNumber(i);
            seat.setCinemaId(cinemaId);


            if (disabledCount > 0) {
                seat.setType("disabled");
                disabledCount--;
            } else if (vipCount > 0) {
                seat.setType("VIP");
                vipCount--;
            } else {
                seat.setType("standard");
            }

            seatDAO.insertSeat(seat);
        }
    }


    private int extractSeatCount(String seatDetails, String seatType) {  //// extract seat for vip,disable

        String pattern = "\\((\\d+) " + seatType + "\\)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(seatDetails);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private int parseSeatCount(String seatDetails) {  ///parse 1 seat to 1

        String[] parts = seatDetails.split(" ");
        return Integer.parseInt(parts[0]);
    }

    private Long getCinema(String cinemaName) {
        log.info("Fetching CinemaDTO for: {} ",cinemaName);
        Long id = cinemaClient.getCinemaIdByName(cinemaName);
        log.info(" CinemaDTO : {} ",id);
        if (id == null) {
            log.info("Cinema not found:{} ", cinemaName);
        }

        return id;
    }

    public int getTotalSeatsByCinemaId(Long cinemaId) {

        return seatDAO.countSeatsByCinemaId(cinemaId);
    }

    public boolean isSeatOccupied(Long seatId) {
        return seatDAO.selectSeatById(seatId)
                .map(Seat::isOccupied)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seat with id [%s] not found".formatted(seatId))
                );
    }

    public void updateSeatOccupation(Long seatId, boolean occupied) {
        Seat seat = seatDAO.selectSeatById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat with id [%s] not found".formatted(seatId)));

        if (seat.isOccupied()) {
            throw new IllegalArgumentException("This seat is already booked.");
        }
        seat.setOccupied(occupied);
        seatDAO.updateSeat(seat);

        if (occupied) {
            NotificationRequest notificationRequest = new NotificationRequest(
                    seat.getCinemaId(), "Seat Booked", "The seat " + seat.getSeatNumber() + " in row " + seat.getRow() + " has been booked."
            );

            rabbitMqMessageProducer.publish(
                    notificationRequest,
                    "internal.exchange",
                    "internal.notification.routing-key"
            );
        }
    }
}