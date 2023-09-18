package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
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
    Routine을 새로 등록하는 기능을 담당합니다. Dto의 데이터를 이용해 먼저 Routine 객체를 등록하고
    해당 Routine 객체를 이용하여 EquipmentRoutine 객체를 등록합니다.
     */
    @PostMapping("")
    public ResponseEntity<Void> registerRoutine(@Valid @RequestBody RegisterRoutineRequest requestDto,
                                                @RequestParam("gym_id") Long gymId) {
        // TODO: remove this authorityId and apply security
        Long authorityId = 1L;

        Authority authority = authorityService.findById(authorityId);

        Routine routine = routineService.registerRoutine(authority, requestDto.getRoutineName());

        Gym gym = gymService.findById(gymId);

        equipmentRoutineService.registerEquipmentRoutine(gym, routine,
                requestDto.getRoutineEquipments());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
    Routine을 업데이트하는 기능을 담당합니다. 루틴 이름이 변경되었다면 먼저 RoutineService에게 로직 실행을 위임합니다.
    그렇지 않다면 바로 EquipmentRoutineService에게 로직 실행을 위임합니다.
     */
    @PatchMapping("/{routineId}")
    public ResponseEntity<Void> updateRoutine(@Valid @RequestBody UpdateRoutineRequest requestDto,
                                              @PathVariable("routineId") Long routineId) {
        if (requestDto.getRoutineName() != null) {
            routineService.updateRoutine(routineId, requestDto.getRoutineName());
        }

        Routine routine = routineService.findById(routineId);
        equipmentRoutineService.updateEquipmentRoutinesInRoutine(routine, requestDto);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /*
    특정 User의 Authority가 생성한 모든 Routine 객체를 조회합니다.
     */
    @GetMapping("")
    public ResponseEntity<RoutineListResponse> findAllRoutinesByAuthority() {
        // TODO: remove this authorityId and apply security
        Long authorityId = 1L;
        Authority authority = authorityService.findById(authorityId);

        RoutineListResponse response = routineService.findAllRoutinesByAuthority(authority);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /*
    특정 Routine을 삭제하는 메소드입니다.
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteRoutine(@Valid @RequestBody DeleteRoutineRequest requestDto) {
        routineService.deleteRoutine(requestDto.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /*
    특정 Routine에 대한 세부사항을 반환하는 메소드입니다.
     */
    @GetMapping("/{routineId}")
    public ResponseEntity<RoutineDetailResponse> findRoutineDetail(@PathVariable("routineId") Long id) {
        RoutineDetailResponse routineDetail = routineService.findRoutineDetail(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routineDetail);
    }
}
