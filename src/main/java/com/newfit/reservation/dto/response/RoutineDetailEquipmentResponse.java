package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.Purpose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineDetailEquipmentResponse {

    private final Long equipmentId;
    private final String name;
    private final Purpose purpose;

    public RoutineDetailEquipmentResponse(Equipment equipment) {
        this(equipment.getId(), equipment.getName(), equipment.getPurpose());
    }
}
