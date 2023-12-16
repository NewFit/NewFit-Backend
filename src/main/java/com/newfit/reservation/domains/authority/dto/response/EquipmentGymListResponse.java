package com.newfit.reservation.domains.authority.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentGymListResponse {
	private final String gymName;
	private final Integer equipmentsCount;
	private final List<EquipmentResponse> equipments;

}
