package com.newfit.reservation.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.request.ObtainCreditRequest;
import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.request.StartReservationRequest;
import com.newfit.reservation.dto.request.routine.RoutineReservationRequest;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.dto.response.RoutineReservationListResponse;
import com.newfit.reservation.dto.response.RoutineReservationResponse;
import com.newfit.reservation.service.AuthorityService;
import com.newfit.reservation.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


@Slf4j
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;
    private final AuthorityService authorityService;
    private final AuthorityCheckService authorityCheckService;

    @PostMapping
    public ResponseEntity<Void> reserve(Authentication authentication,
                                        @RequestHeader("authority-id") Long authorityId,
                                        @RequestParam(value = "equipment_id") Long equipmentId,
                                        @Valid @RequestBody ReservationRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        reservationService.reserve(authorityId, equipmentId, request);
        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<ReservationListResponse> listReservation(@RequestParam("equipment_gym_id") Long equipmentGymId) {

        ReservationListResponse reservationListResponse = reservationService.listReservation(equipmentGymId);
        return ResponseEntity
                .ok(reservationListResponse);
    }

    @PatchMapping
    public ResponseEntity<Void> updateReservation(Authentication authentication,
                                                  @RequestHeader("authority-id") Long authorityId,
                                                  @RequestParam("reservation_id") Long reservationId,
                                                  @Valid @RequestBody ReservationUpdateRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        reservationService.update(reservationId, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(Authentication authentication,
                                                  @RequestHeader("authority-id") Long authorityId,
                                                  @PathVariable Long reservationId) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/routines/{routineId}")
    public ResponseEntity<RoutineReservationListResponse> reserveByRoutine(Authentication authentication,
                                                                           @RequestHeader("authority-id") Long authorityId,
                                                                           @PathVariable Long routineId,
                                                                           @Valid @RequestBody RoutineReservationRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);

        List<RoutineReservationResponse> result = reservationService.reserveByRoutine(authorityId, routineId, request.getStartAt());
        RoutineReservationListResponse response = RoutineReservationListResponse.create(result);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @PatchMapping("/start")
    public ResponseEntity<Void> startOfUse(Authentication authentication,
                                           @RequestHeader("authority-id") Long authorityId,
                                           @Valid @RequestBody StartReservationRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        reservationService.startUse(authorityId, request.getEquipmentGymId(), request.getTagAt());
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/end")
    public ResponseEntity<Void> endOfUseAndObtainCredit(Authentication authentication,
                                                        @RequestHeader(name = "authority-id") Long authorityId,
                                                        @RequestParam(name = "reservation_id") Long reservationId,
                                                        @Valid @RequestBody ObtainCreditRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        Reservation reservation = reservationService.findById(reservationId);
        Authority authority = authorityService.findById(authorityId);

        reservationService.updateStatusAndCondition(reservation);
        reservationService.checkConditionAndAddCredit(reservation, authority, request.getEndEquipmentUseAt());

        return ResponseEntity
                .noContent()
                .build();
    }
}
