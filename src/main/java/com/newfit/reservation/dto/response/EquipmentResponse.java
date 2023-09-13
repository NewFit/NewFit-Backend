package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.Purpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EquipmentResponse {
    private Long id;
    private String name;
    private Purpose purpose;
    private Condition condition;
}
