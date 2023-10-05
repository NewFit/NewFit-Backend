package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.equipment.Purpose;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.service.GymService;
import com.newfit.reservation.service.equipment.EquipmentGymService;
import com.newfit.reservation.service.equipment.EquipmentService;
import com.newfit.reservation.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentApiController {
    private final EquipmentService equipmentService;
    private final GymService gymService;
    private final EquipmentGymService equipmentGymService;
    private final ReservationService reservationService;

    @GetMapping("")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestParam(name = "gym_id") Long gymId,
                                                                    @RequestParam(name = "purpose", required = false) Purpose purpose,
                                                                    @RequestParam(name = "equipment_id", required = false) Long equipmentId) {
        Gym gym = gymService.findById(gymId);
        EquipmentGymListResponse allInGym;

        if (purpose == null && equipmentId == null) { //gymId만 들어온 경우
            allInGym = equipmentGymService.findAllInGym(gym);
        } else if (purpose != null && equipmentId == null) { //gymId와 purpose가 들어온 경우
            allInGym = equipmentGymService.findAllInGymByPurpose(gym, purpose);
        } else if (purpose == null && equipmentId != null) { //gymId와 equipment가 들어온 경우
            Equipment equipment = equipmentService.findById(equipmentId);
            allInGym = equipmentGymService.findAllInGymByEquipment(gym, equipment);
        } else { // 모두 들어온 경우
            return ResponseEntity.badRequest().build();
        }

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
