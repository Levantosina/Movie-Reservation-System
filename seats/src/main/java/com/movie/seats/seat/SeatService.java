package com.movie.seats.seat;





import com.movie.cinema.cinema.CinemaDTO;
import com.movie.seats.exception.ResourceNotFoundException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.io.IOException;
import java.io.InputStreamReader;
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
public class SeatService {

    private final SeatDAO seatDAO;
    private final SeatDTOMapper seatDTOMapper;
    private final Map<String, Long> cinemaMap = new HashMap<>();
    private final RestTemplate restTemplate;

    public SeatService(@Qualifier("seatJdbc") SeatDAO seatDAO, SeatDTOMapper seatDTOMapper, RestTemplate restTemplate) {
        this.seatDAO = seatDAO;
        this.seatDTOMapper = seatDTOMapper;
        this.restTemplate = restTemplate;
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

    public void registerNewSeat(SeatRegistrationRequest seatRegistrationRequest) {
        Seat seat = new Seat();
        seat.setSeatNumber(seatRegistrationRequest.seatNumber());
        seat.setRow(seatRegistrationRequest.row());
        seat.setType(seatRegistrationRequest.type());
        seat.setCinemaId(seatRegistrationRequest.cinemaId());

        seatDAO.insertSeat(seat);
    }

    public List<SeatDTO> getSeatsByCinema(Long cinemaId) {
        return seatDAO.selectSeatsByCinemaId(cinemaId)
                .stream()
                .map(seatDTOMapper)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void loadSeatsFromCSV() {

        String csvFilePath = "cinema_seating_schemes.csv";
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(csvFilePath))))) {
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

                Long cinemaId = getCinemaId(cinemaName);
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
            seat.setSeatNumber(String.valueOf(i));
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

    private Long getCinemaId(String cinemaName) {
        if (cinemaMap.containsKey(cinemaName)) {
            return cinemaMap.get(cinemaName);
        }

        String url = "http://localhost:8081/api/v1/cinemas/name/{cinemaName}";
        CinemaDTO cinemaDTO = restTemplate.getForObject(url, CinemaDTO.class, cinemaName);
        Long cinemaId = (cinemaDTO != null) ? cinemaDTO.cinemaId() : null;
        System.out.println("Retrieved cinema ID for " + cinemaName + ": " + cinemaId);

        if (cinemaId != null) {
            cinemaMap.put(cinemaName, cinemaId);
        }

        return cinemaId;
    }

    public int getTotalSeatsByCinemaId(Long cinemaId) {
        return seatDAO.countSeatsByCinemaId(cinemaId);
    }
}