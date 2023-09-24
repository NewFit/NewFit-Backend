package com.newfit.reservation.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.equipment.Purpose;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterEquipmentRequest {
    @NotBlank
    private String name;

    private Purpose purpose;

    @NotNull
    @Min(value = 1, message = "1개 이상의 기구를 등록해야 합니다.")
    private Integer count;

    @NotNull
    private List<String> equipmentGymNames;
}
