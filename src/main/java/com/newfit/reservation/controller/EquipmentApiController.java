package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.equipment.Purpose;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.service.AuthorityService;
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
    private final AuthorityService authorityService;
    private final EquipmentGymService equipmentGymService;
    private final ReservationService reservationService;

    @GetMapping("")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestHeader(name = "authority-id") Long authorityId,
                                                                    @RequestParam(name = "purpose", required = false) Purpose purpose,
                                                                    @RequestParam(name = "equipment_id", required = false) Long equipmentId) {
        Gym gym = authorityService.getGymByAuthorityId(authorityId);
        EquipmentGymListResponse allInGym;

        if (purpose == null && equipmentId == null) {
            allInGym = equipmentGymService.findAllInGym(gym);
        } else if (purpose != null && equipmentId == null) {
            allInGym = equipmentGymService.findAllInGymByPurpose(gym, purpose);
        } else if (purpose == null && equipmentId != null) {
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
