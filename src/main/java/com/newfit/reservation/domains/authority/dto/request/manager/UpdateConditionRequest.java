package com.newfit.reservation.domains.authority.dto.request.manager;

import com.newfit.reservation.domains.equipment.domain.ConditionType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateConditionRequest {
    @NotNull
    private ConditionType conditionType;
}
