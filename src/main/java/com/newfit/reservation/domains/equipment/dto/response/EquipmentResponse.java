package com.newfit.reservation.domains.equipment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.domain.ConditionType;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentResponse {
    private final Long equipmentId;
    private final Long equipmentGymId;
    private final String equipmentGymName;
    private final PurposeType purpose;
    private final ConditionType condition;

    public EquipmentResponse(EquipmentGym equipmentGym) {
        this.equipmentId = equipmentGym.getEquipment().getId();
        this.equipmentGymId = equipmentGym.getId();
        this.equipmentGymName = equipmentGym.getName();
        this.purpose = equipmentGym.getEquipment().getPurposeType();
        this.condition = equipmentGym.getConditionType();
    }
}
