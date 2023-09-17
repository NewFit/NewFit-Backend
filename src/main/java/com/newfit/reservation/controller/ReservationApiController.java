package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.dto.response.ReservationResponse;
import com.newfit.reservation.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse reserve(@RequestParam(value = "equipment_id") Long equipmentId,
                                       @Valid @RequestBody ReservationRequest request) {
        Long authorityId = 3L;
        return reservationService.reserve(authorityId, equipmentId, request);
    }

    @GetMapping
    public ReservationListResponse listReservation() {
        Long authorityId = 3L;
        return reservationService.listReservation(authorityId);
    }

    @PatchMapping
    public ReservationResponse updateReservation(@RequestParam("reservation_id") Long reservationId,
                                                 @RequestBody ReservationUpdateRequest request){
        return reservationService.update(reservationId, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReservation(@RequestParam("reservation_id") Long reservationId){
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
