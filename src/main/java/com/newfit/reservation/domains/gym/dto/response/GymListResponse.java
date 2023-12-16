package com.newfit.reservation.domains.gym.dto.response;

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
public class GymListResponse {
	private int gymCount;
	private List<GymResponse> gyms;

	@Builder(access = AccessLevel.PRIVATE)
	private GymListResponse(List<GymResponse> gyms) {
		this.gymCount = gyms.size();
		this.gyms = gyms;
	}

	public static GymListResponse createResponse(List<GymResponse> gyms) {
		return GymListResponse.builder()
			.gyms(gyms)
			.build();
	}
}
