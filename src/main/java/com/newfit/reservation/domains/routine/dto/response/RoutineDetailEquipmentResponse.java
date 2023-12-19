package com.newfit.reservation.domains.routine.dto.response;

import java.time.Duration;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.PurposeType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineDetailEquipmentResponse {

	private final Long equipmentId;
	private final String name;
	private final PurposeType purpose;
	private final Long duration;

	@Builder(access = AccessLevel.PRIVATE)
	private RoutineDetailEquipmentResponse(Equipment equipment, Duration duration) {
		this(equipment.getId(), equipment.getName(), equipment.getPurposeType(), duration.toMinutes());
	}

	public static RoutineDetailEquipmentResponse create(Equipment equipment, Duration duration) {
		return RoutineDetailEquipmentResponse.builder()
			.equipment(equipment)
			.duration(duration)
			.build();
	}
}
