package com.newfit.reservation.domains.equipment.controller;

import com.newfit.reservation.domains.authority.service.AuthorityService;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.domains.equipment.service.EquipmentGymService;
import com.newfit.reservation.domains.equipment.service.EquipmentService;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.reservation.service.ReservationService;
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
    private final AuthorityService authorityService;
    private final EquipmentGymService equipmentGymService;
    private final ReservationService reservationService;

    @GetMapping("")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestHeader(name = "authority-id") Long authorityId,
                                                                    @RequestParam(name = "purposeType", required = false) PurposeType purposeType,
                                                                    @RequestParam(name = "equipment_id", required = false) Long equipmentId) {
        Gym gym = authorityService.getGymByAuthorityId(authorityId);
        EquipmentGymListResponse allInGym;

        if (purposeType == null && equipmentId == null) {
            allInGym = equipmentGymService.findAllInGym(gym);
        } else if (purposeType != null && equipmentId == null) {
            allInGym = equipmentGymService.findAllInGymByPurpose(gym, purposeType);
        } else if (purposeType == null && equipmentId != null) {
            Equipment equipment = equipmentService.findById(equipmentId);
            allInGym = equipmentGymService.findAllInGymByEquipment(gym, equipment);
        } else {
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
