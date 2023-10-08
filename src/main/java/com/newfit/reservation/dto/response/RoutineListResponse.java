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
public class RoutineListResponse {

    private int routinesCount;
    private List<RoutineResponse> routines;

    @Builder(access = AccessLevel.PRIVATE)
    private RoutineListResponse(List<RoutineResponse> routines) {
        this.routinesCount = routines.size();
        this.routines = routines;
    }

    public static RoutineListResponse createResponse(List<RoutineResponse> routines) {
        return RoutineListResponse.builder()
                .routines(routines)
                .build();
    }
}
