package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentInfoResponse {
    private final String gymName;
    private final String equipmentName;
    private final Integer occupiedTimesCount;
    private final List<OccupiedTime> occupiedTimes;

    public EquipmentInfoResponse(EquipmentGym equipmentGym, List<OccupiedTime> occupiedTimes) {
        this.gymName = equipmentGym.getGym().getName();
        this.equipmentName = equipmentGym.getEquipment().getName();
        this.occupiedTimesCount = occupiedTimes.size();
        this.occupiedTimes = occupiedTimes;
    }
}