package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.request.RegisterRoutineRequest;
import com.newfit.reservation.dto.request.UpdateRoutineRequest;
import com.newfit.reservation.dto.response.RoutineListResponse;
import com.newfit.reservation.service.GymService;
import com.newfit.reservation.service.UserService;
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
    private final UserService userService;
    private final GymService gymService;

    /* 
    Routine을 새로 등록하는 기능을 담당합니다. Dto의 데이터를 이용해 먼저 Routine 객체를 등록하고
    해당 Routine 객체를 이용하여 EquipmentRoutine 객체를 등록합니다. 그리고 생성된 Routine 객체의 id를 반환합니다.
     */
    @PostMapping("")
    public ResponseEntity<Void> registerRoutine(@Valid @RequestBody RegisterRoutineRequest requestDto) {
        // TODO: remove this userId and apply security
        Long userId = 1L;

        User findUser = userService.findOneById(userId)
                .orElseThrow(IllegalArgumentException::new);

        Routine routine = routineService.registerRoutine(findUser, requestDto.getName());

        Gym findGym = gymService.findById(requestDto.getGymId());

        equipmentRoutineService.registerEquipmentRoutine(findGym, routine,
                requestDto.getRoutineEquipments());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
    Routine을 업데이트하는 기능을 담당합니다. RoutineService에게 로직 실행을 위임합니다.
     */
    @PatchMapping("")
    public ResponseEntity<Void> updateRoutine(@Valid @RequestBody UpdateRoutineRequest requestDto) {
        Routine findRoutine = routineService.findById(requestDto.getRoutineId());
        Gym findGym = gymService.findById(requestDto.getGymId());

        routineService.updateRoutine(findGym, findRoutine, requestDto.getRoutineEquipments());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("")
    public ResponseEntity getAllRoutinesByUser() {
        // TODO: remove this userId and apply security
        Long userId = 1L;
        User findUser = userService.findOneById(userId)
                .orElseThrow(IllegalArgumentException::new);

        RoutineListResponse response = routineService.getAllRoutinesByUser(findUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
