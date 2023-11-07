package com.newfit.reservation.domains.equipment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentInfoResponse {
    private final String gymName;
    private final Long equipmentGymId;
    private final String equipmentGymName;
    private final Integer occupiedTimesCount;
    private final List<OccupiedTime> occupiedTimes;

    public EquipmentInfoResponse(EquipmentGym equipmentGym, List<OccupiedTime> occupiedTimes) {
        this.gymName = equipmentGym.getGym().getName();
        this.equipmentGymId = equipmentGym.getId();
        this.equipmentGymName = equipmentGym.getName();
        this.occupiedTimesCount = occupiedTimes.size();
        this.occupiedTimes = occupiedTimes;
    }
}
