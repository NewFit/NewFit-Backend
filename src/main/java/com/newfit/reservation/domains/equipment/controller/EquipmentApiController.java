package com.newfit.reservation.domains.equipment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.dto.request.EquipmentQueryRequest;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.domains.equipment.service.EquipmentGymService;
import com.newfit.reservation.domains.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentApiController {
	private final EquipmentGymService equipmentGymService;
	private final ReservationService reservationService;

	@GetMapping
	public ResponseEntity<EquipmentGymListResponse> getAllEquipment(
		@RequestHeader(name = "authority-id") Long authorityId, EquipmentQueryRequest request) {
		EquipmentGymListResponse allInGym = equipmentGymService.findAllByQuery(authorityId, request);

		return ResponseEntity
			.ok(allInGym);
	}

	@GetMapping("/{equipmentGymId}")
	public ResponseEntity<EquipmentInfoResponse> getEquipmentInfo(@PathVariable Long equipmentGymId) {
		EquipmentGym equipmentGym = equipmentGymService.findOneById(equipmentGymId);
		EquipmentInfoResponse allOccupiedTimes = reservationService.getAllOccupiedTimes(equipmentGym);

		return ResponseEntity
			.ok(allOccupiedTimes);
	}
}
