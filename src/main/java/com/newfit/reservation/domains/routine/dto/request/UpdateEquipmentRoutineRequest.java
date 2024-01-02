package com.newfit.reservation.domains.routine.dto.request;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateEquipmentRoutineRequest {

	@NotNull
	private int equipmentsCount;

	@NotNull
	private List<EquipmentRoutineRequest> equipments;
}
