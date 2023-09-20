package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineReservationResponse {
    private final Long equipmentGymId;
    private final Boolean success;
    private final LocalDateTime startAt;

    public Boolean isSuccess() {
        return this.success;
    }
}
