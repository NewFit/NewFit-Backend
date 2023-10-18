package com.newfit.reservation.domains.equipment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentGymListResponse {
    private final String gymName;
    private final Integer equipmentsCount;
    private final List<EquipmentResponse> equipments;

    @Builder(access = AccessLevel.PRIVATE)
    private EquipmentGymListResponse(String gymName, List<EquipmentResponse> equipments) {
        this.gymName = gymName;
        this.equipmentsCount = equipments.size();
        this.equipments = equipments;
    }

    public static EquipmentGymListResponse createResponse(String gymName, List<EquipmentResponse> equipments) {
        return EquipmentGymListResponse
                .builder()
                .gymName(gymName)
                .equipments(equipments)
                .build();
    }
}
