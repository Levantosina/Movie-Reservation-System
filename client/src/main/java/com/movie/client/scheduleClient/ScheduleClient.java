package com.movie.client.scheduleClient;

import com.movie.common.ScheduleDTO;
import com.movie.jwt.jwt.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@FeignClient(name = "schedule",configuration = FeignConfig.class)
public interface ScheduleClient {

    @GetMapping(value = "/api/v1/schedules/{scheduleId}")
    ScheduleDTO getScheduleById(@PathVariable("scheduleId") Long scheduleId);
    @PutMapping("/api/v1/schedules/{scheduleId}/decrease")
    void decreaseAvailableSeats(@PathVariable("scheduleId") Long scheduleId);
    @GetMapping("/api/v1/schedules/startTime/{scheduleId}")
    String getStartTime(@PathVariable("scheduleId") Long scheduleId);
}
