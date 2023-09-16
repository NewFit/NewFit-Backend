package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.dto.response.ReservationResponse;
import com.newfit.reservation.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;


    @PostMapping
    public ReservationResponse reserve(@RequestParam(value = "equipment_id") Long equipmentId,
                                       @Valid @RequestBody ReservationRequest request) {
        Long userId = 5L;
        return reservationService.reserve(userId, equipmentId, request);
    }

    @GetMapping
    public ReservationListResponse listReservation(@RequestParam(value = "gym_id") Long gymId) {
        Long userId = 5L;
        return reservationService.listReservation(userId, gymId);
    }
}
