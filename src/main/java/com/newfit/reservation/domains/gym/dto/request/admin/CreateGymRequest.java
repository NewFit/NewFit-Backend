package com.newfit.reservation.domains.gym.dto.request.admin;

import java.time.LocalTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateGymRequest {
	private String name;
	private String tel;
	private String address;
	private LocalTime openAt;
	private LocalTime closeAt;
	private Boolean allDay;

}