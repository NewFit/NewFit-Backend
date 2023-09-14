package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.Purpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EquipmentResponse {
    private final Long id;
    private final String name;
    private final Purpose purpose;
    private final Condition condition;
}
