package com.newfit.reservation.controller;

import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gyms")
public class GymApiController {

    private final GymService gymService;

    @GetMapping
    public ResponseEntity<GymListResponse> searchGyms(@RequestParam(value = "gym_name", required = false) String gymName) {
        GymListResponse response = gymService.searchGyms(gymName);
        return ResponseEntity
                .ok(response);
    }
}
