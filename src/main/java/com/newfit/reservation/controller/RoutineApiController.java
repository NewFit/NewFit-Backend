package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.request.DeleteRoutineRequest;
import com.newfit.reservation.dto.request.RegisterRoutineRequest;
import com.newfit.reservation.dto.request.UpdateRoutineRequest;
import com.newfit.reservation.dto.response.RoutineDetailResponse;
import com.newfit.reservation.dto.response.RoutineListResponse;
import com.newfit.reservation.service.AuthorityService;
import com.newfit.reservation.service.GymService;
import com.newfit.reservation.service.routine.EquipmentRoutineService;
import com.newfit.reservation.service.routine.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/routines")
public class RoutineApiController {

    private final RoutineService routineService;
    private final EquipmentRoutineService equipmentRoutineService;
    private final AuthorityService authorityService;
    private final GymService gymService;

    /* 
    Routine을 새로 등록
    Routine 객체 등록 후 EquipmentRoutine들 등록
     */
    @PostMapping("")
    public ResponseEntity<Void> registerRoutine(@Valid @RequestBody RegisterRoutineRequest request) {
        // TODO: remove this authorityId and apply security
        Long authorityId = 1L;

        Authority authority = authorityService.findById(authorityId);

        Routine routine = routineService.registerRoutine(authority, request.getRoutineName());

        equipmentRoutineService.registerEquipmentRoutine(routine, request.getRoutineEquipments());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
    Routine을 업데이트
    루틴 이름이 변경 =>  먼저 RoutineService로 로직 실행
    EquipmentRoutineService로 로직 실행
     */
    @PatchMapping("/{routineId}")
    public ResponseEntity<Void> updateRoutine(@Valid @RequestBody UpdateRoutineRequest request,
                                              @PathVariable("routineId") Long routineId) {
        if (request.getRoutineName() != null) {
            routineService.updateRoutine(routineId, request.getRoutineName());
        }

        Routine routine = routineService.findById(routineId);
        equipmentRoutineService.updateEquipmentRoutinesInRoutine(routine, request);

        return ResponseEntity
                .noContent()
                .build();
    }

    /*
    Authority가 생성한 모든 Routine 조회
     */
    @GetMapping("")
    public ResponseEntity<RoutineListResponse> findAllRoutinesByAuthority() {
        // TODO: remove this authorityId and apply security
        Long authorityId = 1L;
        Authority authority = authorityService.findById(authorityId);

        RoutineListResponse response = routineService.findAllRoutinesByAuthority(authority);

        return ResponseEntity
                .ok(response);
    }

    /*
    특정 Routine을 삭제
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteRoutine(@Valid @RequestBody DeleteRoutineRequest request) {
        routineService.deleteRoutine(request.getRoutineId());
        return ResponseEntity
                .noContent()
                .build();
    }

    /*
    특정 Routine에 대한 세부사항을 반환하는 메소드
     */
    @GetMapping("/{routineId}")
    public ResponseEntity<RoutineDetailResponse> findRoutineDetail(@PathVariable("routineId") Long id) {
        RoutineDetailResponse routineDetail = routineService.findRoutineDetail(id);
        return ResponseEntity
                .ok(routineDetail);
    }
}
