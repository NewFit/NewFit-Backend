package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.equipment.Condition;
import lombok.Getter;

@Getter
public class UpdateConditionRequest {
    private Condition condition;
}
