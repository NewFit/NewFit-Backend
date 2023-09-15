package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.equipment.Condition;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateConditionRequest {
    @NotNull
    private Condition condition;
}
