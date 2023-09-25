package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.equipment.Purpose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentResponse {
    private final Long id;
    private final String equipmentGymName;
    private final Purpose purpose;
    private final Condition condition;

    public EquipmentResponse(EquipmentGym equipmentGym) {
        this.id = equipmentGym.getId();
        this.equipmentGymName = equipmentGym.getName();
        this.purpose = equipmentGym.getEquipment().getPurpose();
        this.condition = equipmentGym.getCondition();
    }
}
