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

    private Long routineId;
    private String routineName;
    private int equipmentsCount;
    private List<RoutineDetailEquipmentResponse> equipments;

    @Builder(access = AccessLevel.PRIVATE)
    private RoutineDetailResponse(Long routineId, String routineName, List<RoutineDetailEquipmentResponse> equipments) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.equipmentsCount = equipments.size();
        this.equipments = equipments;
    }

    public static RoutineDetailResponse createResponse(Long routineId, String routineName,
                                                       List<RoutineDetailEquipmentResponse> equipments) {
        return RoutineDetailResponse.builder()
                .routineId(routineId)
                .routineName(routineName)
                .equipments(equipments)
                .build();
    }
}
