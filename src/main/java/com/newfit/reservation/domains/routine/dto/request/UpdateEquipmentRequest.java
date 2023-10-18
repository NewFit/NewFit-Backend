package com.newfit.reservation.domains.routine.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateEquipmentRequest {   // 사용자가 기존 루틴에서 수정한 데이터를 받습니다.

    @NotNull
    private Long equipmentId;

    @Max(value = 30)
    private Long duration;

    private Short sequence;
}
