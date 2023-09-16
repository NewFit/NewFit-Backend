package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.Purpose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDetailEquipmentResponse {

    private final Long id;
    private final String name;
    private final Purpose purpose;

    public RoutineDetailEquipmentResponse(Equipment equipment) {
        this(equipment.getId(), equipment.getName(), equipment.getPurpose());
    }
}
