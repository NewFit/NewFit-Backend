package com.newfit.reservation.domains.authority.dto.request.manager;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterEquipmentRequest {
    @NotBlank
    private String name;

    private PurposeType purposeType;

    @NotNull
    @Min(value = 1, message = "1개 이상의 기구를 등록해야 합니다.")
    private Integer count;

    @NotNull
    private List<String> equipmentGymNames;
}
