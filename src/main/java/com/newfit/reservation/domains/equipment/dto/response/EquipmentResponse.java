package com.newfit.reservation.domains.equipment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.domain.Condition;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.Purpose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentResponse {
    private final Long equipmentId;
    private final Long equipmentGymId;
    private final String equipmentGymName;
    private final Purpose purpose;
    private final Condition condition;

    public EquipmentResponse(EquipmentGym equipmentGym) {
        this.equipmentId = equipmentGym.getEquipment().getId();
        this.equipmentGymId = equipmentGym.getId();
        this.equipmentGymName = equipmentGym.getName();
        this.purpose = equipmentGym.getEquipment().getPurpose();
        this.condition = equipmentGym.getCondition();
    }
}
