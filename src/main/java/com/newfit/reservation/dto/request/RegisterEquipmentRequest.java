package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.equipment.Purpose;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterEquipmentRequest {
    @NotBlank
    private String name;

    private Purpose purpose;

    @NotNull
    @Min(value = 1, message = "1개 이상의 기구를 등록해야 합니다.")
    private Integer count;
}
