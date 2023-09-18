package com.newfit.reservation.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.dto.request.routine.AddEquipmentRequest;
import com.newfit.reservation.dto.request.routine.RemoveEquipmentRequest;
import com.newfit.reservation.dto.request.routine.UpdateEquipmentRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateRoutineRequest {

    private String routineName;

    @NotNull
    private int addEquipmentsCount;

    @NotNull
    private int updateEquipmentsCount;

    @NotNull
    private int removeEquipmentsCount;
    private List<AddEquipmentRequest> addEquipments;
    private List<UpdateEquipmentRequest> updateEquipments;
    private List<RemoveEquipmentRequest> removeEquipments;
}
