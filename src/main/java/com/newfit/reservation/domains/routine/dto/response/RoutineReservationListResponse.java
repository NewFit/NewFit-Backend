package com.newfit.reservation.domains.routine.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineReservationListResponse {
	private int successCount;
	private List<RoutineReservationResponse> successes;

	@Builder(access = AccessLevel.PRIVATE)
	private RoutineReservationListResponse(List<RoutineReservationResponse> successes) {
		this.successCount = successes.size();
		this.successes = successes;
	}

	public static RoutineReservationListResponse create(List<RoutineReservationResponse> result) {
		return RoutineReservationListResponse.builder()
			.successes(result)
			.build();
	}
}
