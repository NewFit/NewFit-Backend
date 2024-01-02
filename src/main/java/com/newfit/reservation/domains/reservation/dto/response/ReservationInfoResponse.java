package com.newfit.reservation.domains.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.reservation.domain.Reservation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationInfoResponse {
	private String gymName;
	private Long equipmentGymId;
	private ReservationDetailResponse reservation;

	@Builder(access = AccessLevel.PRIVATE)
	private ReservationInfoResponse(String gymName, Reservation reservation) {
		this.gymName = gymName;
		this.equipmentGymId = reservation.getEquipmentGym().getId();
		this.reservation = new ReservationDetailResponse(reservation);
	}

	public static ReservationInfoResponse createResponse(String gymName, Reservation reservation) {
		return ReservationInfoResponse
			.builder()
			.gymName(gymName)
			.reservation(reservation)
			.build();
	}
}
