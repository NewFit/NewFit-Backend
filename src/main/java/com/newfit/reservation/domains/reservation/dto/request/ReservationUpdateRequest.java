package com.newfit.reservation.domains.reservation.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationUpdateRequest {
	@Min(1)
	private Long equipmentGymId;

	private LocalDateTime startAt;
	
	@Future
	private LocalDateTime endAt;
}
