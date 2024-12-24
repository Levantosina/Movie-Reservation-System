package com.movie.client.scheduleClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DMITRII LEVKIN on 24/12/2024
 * @project Movie-Reservation-System
 */
@FeignClient("schedules")
public interface ScheduleClient {

    @GetMapping(value = "/api/v1/schedules/{id}")
    Long getScheduleById(@PathVariable("id") Long scheduleId);
}
