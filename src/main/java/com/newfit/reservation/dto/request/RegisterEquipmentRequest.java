package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.equipment.Purpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEquipmentRequest {
    private String name;
    private Purpose purpose;
    private Integer count;
}
