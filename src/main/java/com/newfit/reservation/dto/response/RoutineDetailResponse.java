package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineDetailResponse {

    private Long id;
    private String name;
    private int equipmentsCount;
    private List<RoutineDetailEquipmentResponse> equipments;

    @Builder
    private RoutineDetailResponse(Long id, String name, List<RoutineDetailEquipmentResponse> equipments) {
        this.id = id;
        this.name = name;
        this.equipmentsCount = equipments.size();
        this.equipments = equipments;
    }
}
