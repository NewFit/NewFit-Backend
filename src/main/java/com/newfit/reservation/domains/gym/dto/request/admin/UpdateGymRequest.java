package com.newfit.reservation.domains.gym.dto.request.admin;

import java.time.LocalTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.gym.domain.Gym;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateGymRequest {
	private String name;
	private String tel;
	private String address;
	private LocalTime openAt;
	private LocalTime closeAt;
	private Boolean allDay;

	@Builder(access = AccessLevel.PRIVATE)
	private UpdateGymRequest(String name, String tel, String address, LocalTime openAt, LocalTime closeAt,
		Boolean allDay) {
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.openAt = openAt;
		this.closeAt = closeAt;
		this.allDay = allDay;
	}

	public static UpdateGymRequest from(Gym gym) {
		return UpdateGymRequest.builder()
			.name(gym.getName())
			.tel(gym.getTel())
			.address(gym.getAddress())
			.openAt(gym.getBusinessTime().getOpenAt())
			.closeAt(gym.getBusinessTime().getCloseAt())
			.allDay(gym.getBusinessTime().isAllDay())
			.build();
	}
}
