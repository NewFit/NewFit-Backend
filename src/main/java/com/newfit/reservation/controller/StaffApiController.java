package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.dto.request.RegisterEquipmentRequest;
import com.newfit.reservation.dto.request.UpdateConditionRequest;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
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
    public ResponseEntity<Void> registerEquipment(@Valid @RequestBody RegisterEquipmentRequest request) {
        // TODO: remove this userId and apply security
        Long userId = 1L;
        Gym gym = authorityService.getGym(userId);

        Equipment equipment = equipmentService.registerEquipment(gym, request.getName(), request.getPurpose());

        equipmentGymService.registerEquipmentInGym(equipment, gym, request.getCount());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
    모든 Gym을 조회하여 반환
     */
    @GetMapping("/equipment-gym")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestParam(name = "gym_id") Long gymId) {
        Gym gym = gymService.findById(gymId).get();
        EquipmentGymListResponse allInGym = equipmentGymService.findAllInGym(gym);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allInGym);
    }

    /*
    condition 수정
     */
    @PatchMapping("/equipment-gym/{equipmentGymId}")
    public ResponseEntity<Void> updateEquipmentCondition(@PathVariable Long equipmentGymId, @RequestBody UpdateConditionRequest request) {
        equipmentGymService.updateCondition(equipmentGymId, request.getCondition());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
