package com.newfit.reservation.controller;

import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gyms")
public class GymApiController {

    private final GymService gymService;

    // NewFit 서비스에 등록된 모든 헬스장을 반환합니다.
    @GetMapping("")
    public ResponseEntity<GymListResponse> getAllGyms() {
        GymListResponse gymListResponse = gymService.getAllGyms();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gymListResponse);
    }
}
