package com.newfit.reservation.domains.routine.controller;

import org.springframework.http.HttpStatus;
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
import com.newfit.reservation.domains.routine.domain.Routine;
import com.newfit.reservation.domains.routine.dto.request.DeleteRoutineRequest;
import com.newfit.reservation.domains.routine.dto.request.RegisterRoutineRequest;
import com.newfit.reservation.domains.routine.dto.request.UpdateEquipmentRoutineRequest;
import com.newfit.reservation.domains.routine.dto.request.UpdateRoutineNameRequest;
import com.newfit.reservation.domains.routine.dto.response.RoutineDetailResponse;
import com.newfit.reservation.domains.routine.dto.response.RoutineListResponse;
import com.newfit.reservation.domains.routine.service.EquipmentRoutineService;
import com.newfit.reservation.domains.routine.service.RoutineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routines")
public class RoutineApiController {

	private final RoutineService routineService;
	private final EquipmentRoutineService equipmentRoutineService;
	private final AuthorityService authorityService;
	private final AuthorityCheckService authorityCheckService;

	/*
	Routine을 새로 등록
	Routine 객체 등록 후 EquipmentRoutine들 등록
	 */
	@PostMapping("")
	public ResponseEntity<Void> registerRoutine(Authentication authentication,
		@RequestHeader(value = "authority-id") Long authorityId,
		@Valid @RequestBody RegisterRoutineRequest request) {
		authorityCheckService.validateByAuthorityId(authentication, authorityId);
		Authority authority = authorityService.findById(authorityId);

		Routine routine = routineService.registerRoutine(authority, request.getRoutineName());

		equipmentRoutineService.registerEquipmentRoutine(routine, request.getRoutineEquipments());

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

	/*
	Routine을 업데이트
	루틴 이름만 변경
	 */
	@PatchMapping("/{routineId}")
	public ResponseEntity<Void> updateRoutine(Authentication authentication,
		@RequestHeader(value = "authority-id") Long authorityId,
		@Valid @RequestBody UpdateRoutineNameRequest request,
		@PathVariable("routineId") Long routineId) {
		authorityCheckService.validateByAuthorityId(authentication, authorityId);
		routineService.updateRoutineName(routineId, request);

		return ResponseEntity
			.noContent()
			.build();
	}

	@PatchMapping("/equipments")
	public ResponseEntity<Void> updateEquipmentInRoutine(Authentication authentication,
		@RequestHeader(value = "authority-id") Long authorityId,
		@Valid @RequestBody UpdateEquipmentRoutineRequest request,
		@RequestParam("routine_id") Long routineId) {
		authorityCheckService.validateByAuthorityId(authentication, authorityId);
		equipmentRoutineService.updateEquipmentRoutinesInRoutine(routineId, request);

		return ResponseEntity
			.noContent()
			.build();
	}

	/*
	Authority가 생성한 모든 Routine 조회
	 */
	@GetMapping("")
	public ResponseEntity<RoutineListResponse> findAllRoutinesByAuthority(
		@RequestHeader(value = "authority-id") Long authorityId) {
		Authority authority = authorityService.findById(authorityId);

		RoutineListResponse response = routineService.findAllRoutinesByAuthority(authority);

		return ResponseEntity
			.ok(response);
	}

	/*
	특정 Routine을 삭제
	 */
	@DeleteMapping("")
	public ResponseEntity<Void> deleteRoutine(Authentication authentication,
		@RequestHeader(value = "authority-id") Long authorityId,
		@Valid @RequestBody DeleteRoutineRequest request) {
		authorityCheckService.validateByAuthorityId(authentication, authorityId);
		routineService.deleteRoutine(request.getRoutineId());
		return ResponseEntity
			.noContent()
			.build();
	}

	/*
	특정 Routine에 대한 세부사항을 반환하는 메소드
	 */
	@GetMapping("/{routineId}")
	public ResponseEntity<RoutineDetailResponse> findRoutineDetail(@PathVariable("routineId") Long routineId) {
		RoutineDetailResponse routineDetail = routineService.findRoutineDetail(routineId);
		return ResponseEntity
			.ok(routineDetail);
	}
}
