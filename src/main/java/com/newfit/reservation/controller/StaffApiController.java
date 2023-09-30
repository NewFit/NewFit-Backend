package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.Role;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.dto.request.*;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.dto.response.UserAcceptResponse;
import com.newfit.reservation.dto.response.UserAndPendingListResponse;
import com.newfit.reservation.service.AuthorityService;
import com.newfit.reservation.service.GymService;
import com.newfit.reservation.service.equipment.EquipmentGymService;
import com.newfit.reservation.service.equipment.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffApiController {
    private final EquipmentService equipmentService;
    private final AuthorityService authorityService;
    private final EquipmentGymService equipmentGymService;
    private final GymService gymService;

    /*
    user의 gym을 조회
    gym에 equipment 등록
    equipmentGym에 count만큼 등록
     */
    @PostMapping("/equipments")
    public ResponseEntity<Void> registerEquipment(@RequestParam(name = "gym_id") Long gymId, @Valid @RequestBody RegisterEquipmentRequest request) {
        // TODO: remove this userId and apply security
        Long userId = 1L;
        Gym gym = authorityService.getGym(userId, gymId, Role.MANAGER);

        Equipment equipment = equipmentService.registerEquipment(gym, request.getName(), request.getPurpose());

        equipmentGymService.registerEquipmentInGym(equipment, gym, request.getCount(), request.getEquipmentGymNames());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
    모든 Gym을 조회하여 반환
     */
    @GetMapping("/equipment-gym")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestParam(name = "gym_id") Long gymId) {
        Gym gym = gymService.findById(gymId);
        EquipmentGymListResponse allInGym = equipmentGymService.findAllInGym(gym);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allInGym);
    }

    /*
    condition 수정
     */
    @PatchMapping("/equipment-gym/{equipmentGymId}")
    public ResponseEntity<Void> updateEquipmentCondition(@PathVariable Long equipmentGymId, @Valid @RequestBody UpdateConditionRequest request) {
        equipmentGymService.updateCondition(equipmentGymId, request.getCondition());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /*
    equipment를 삭제
    cascade 설정에 따라 연관된 EquipmentGym도 삭제
     */
    @DeleteMapping("/equipments")
    public ResponseEntity<Void> deleteEquipment(@Valid @RequestBody DeleteEquipmentRequest request) {
        equipmentService.deleteEquipment(request.getEquipmentId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /*
    특정 EquipmentGym 삭제
     */
    @DeleteMapping("/equipment-gym")
    public ResponseEntity<Void> deleteEquipmentGym(@Valid @RequestBody DeleteEquipmentGymRequest request) {
        equipmentGymService.deleteEquipmentGym(request.getEquipmentGymId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /*
    userId와 gymId를 받아서 해당 유저를 승인하는 메소드입니다.
    AuthorityService에게 로직 실행을 위임한 뒤에 Dto를 넘겨받아 반환합니다.
     */
    @PostMapping("/authority")
    public ResponseEntity<UserAcceptResponse> acceptUser(@Valid @RequestBody UserAcceptRequest requestDto) {
        UserAcceptResponse userAcceptResponse = authorityService.acceptUser(requestDto.getUserId(), requestDto.getGymId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAcceptResponse);
    }

    /*
    특정 Gym을 이용하는 유저 및 승인 요청을 보낸 유저를 리스트 형식으로 반환하는 메소드입니다.
    마찬가지로 AuthorityService에게 로직 실행을 위임한 뒤에 Dto를 넘겨받아 반환합니다.
     */
    @GetMapping("/authority")
    public ResponseEntity<UserAndPendingListResponse> getUserAndAcceptRequestList(@RequestParam("gym_id") Long gymId) {
        UserAndPendingListResponse responseDto = authorityService.getUserAndAcceptRequestList(gymId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
