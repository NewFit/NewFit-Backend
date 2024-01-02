package com.newfit.reservation.domains.reservation.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.service.AuthorityService;
import com.newfit.reservation.domains.credit.dto.request.ObtainCreditRequest;
import com.newfit.reservation.domains.credit.service.CreditService;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.domains.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.domains.reservation.dto.request.StartReservationRequest;
import com.newfit.reservation.domains.reservation.dto.response.EquipmentGymsListResponse;
import com.newfit.reservation.domains.reservation.dto.response.ReservationInfoResponse;
import com.newfit.reservation.domains.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.domains.reservation.service.ReservationService;
import com.newfit.reservation.domains.routine.dto.request.RoutineReservationRequest;
import com.newfit.reservation.domains.routine.dto.response.RoutineReservationListResponse;
import com.newfit.reservation.domains.routine.service.RoutineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationApiController {

	private final AuthorityService authorityService;
	private final AuthorityCheckService authorityCheckService;
	private final CreditService creditService;
	private final ReservationService reservationService;
	private final RoutineService routineService;

	@PostMapping("/{equipmentGymId}")
	public ResponseEntity<Void> reserve(Authentication authentication,
		@RequestHeader("authority-id") Long authorityId,
		@PathVariable("equipmentGymId") Long equipmentGymId,
		@Valid @RequestBody ReservationRequest request) {
		authorityCheckService.validateByAuthorityId(authentication, authorityId);
		reservationService.reserve(authorityId, equipmentGymId, request);
		return ResponseEntity
			.status(CREATED)
			.build();
	}

	@GetMapping
	public ResponseEntity<ReservationListResponse> listReservation(
		@RequestParam("equipment_gym_id") Long equipmentGymId) {

		ReservationListResponse reservationListResponse = reservationService.listReservation(equipmentGymId);
		return ResponseEntity
			.ok(reservationListResponse);
	}

	@PatchMapping("/{reservationId}")
	public ResponseEntity<Void> updateReservation(Authentication authentication,
		@RequestHeader("authority-id") Long authorityId,
		@PathVariable("reservationId") Long reservationId,
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
		RoutineReservationListResponse response = routineService.reserveByRoutine(authorityId, routineId,
			request.getStartAt());

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
		creditService.checkConditionAndAddCredit(reservation, authority, request.getEndEquipmentUseAt());

		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping("/{reservationId}")
	public ResponseEntity<ReservationInfoResponse> getReservation(@PathVariable Long reservationId) {
		ReservationInfoResponse reservationInfo = reservationService.getReservationInfo(reservationId);

		return ResponseEntity
			.ok(reservationInfo);
	}

	@GetMapping("/equipments/{equipmentId}")
	public ResponseEntity<EquipmentGymsListResponse> getEquipmentsInfo(@PathVariable Long equipmentId) {
		EquipmentGymsListResponse response = reservationService.getEquipmentsInfo(equipmentId);

		return ResponseEntity
			.ok(response);
	}
}
