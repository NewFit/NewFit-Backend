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
public class GymListResponse {
    private int gymCount;
    private List<GymResponseDto> gyms;

    @Builder
    private GymListResponse(List<GymResponseDto> gyms) {
        this.gymCount = gyms.size();
        this.gyms = gyms;
    }

}
