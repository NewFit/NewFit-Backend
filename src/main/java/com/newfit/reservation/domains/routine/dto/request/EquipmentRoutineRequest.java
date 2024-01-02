package com.newfit.reservation.domains.routine.dto.request;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentRoutineRequest {  // 사용자가 루틴에 새로 추가한 데이터를 받습니다.

	@NotNull
	private Long equipmentId;

	@NotNull
	@Range(max = 30)
	private Long duration;
}
