package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.request.StartReservationRequest;
import com.newfit.reservation.dto.request.routine.RoutineReservationRequest;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.dto.response.RoutineReservationListResponse;
import com.newfit.reservation.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.CREATED;


@Slf4j
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity<Void> reserve(@RequestHeader("authority-id") Long authorityId,
                                       @RequestParam(value = "equipment_id") Long equipmentId,
                                       @Valid @RequestBody ReservationRequest request) {
        reservationService.reserve(authorityId, equipmentId, request);
        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @GetMapping
    public ReservationListResponse listReservation(@RequestParam("equipment_gym_id") Long equipmentGymId) {

        return reservationService.listReservation(equipmentGymId);
    }

    @PatchMapping
    public ResponseEntity<Void> updateReservation(@RequestParam("reservation_id") Long reservationId,
                                                 @Valid @RequestBody ReservationUpdateRequest request) {
        reservationService.update(reservationId, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReservation(@RequestParam("reservation_id") Long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/routines/{routineId}")
    public ResponseEntity<RoutineReservationListResponse> reserveByRoutine(@PathVariable Long routineId, @Valid @RequestBody RoutineReservationRequest request) {
        Long authorityId = 1L;
        RoutineReservationListResponse response = new RoutineReservationListResponse(reservationService.reserveByRoutine(authorityId, routineId, request.getStartAt()));

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @PatchMapping("/start")
    public ResponseEntity<Void> startOfUse(@RequestHeader("authority-id") Long authorityId, @Valid @RequestBody StartReservationRequest request) {
        reservationService.startUse(authorityId, request.getEquipmentGymId(), request.getTagAt());
        return ResponseEntity
                .noContent()
                .build();
    }
}
