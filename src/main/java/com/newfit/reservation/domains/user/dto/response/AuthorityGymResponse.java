package com.newfit.reservation.domains.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.authority.domain.Authority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthorityGymResponse {

	private final Long authorityId;
	private final String gymName;

	public AuthorityGymResponse(Authority authority) {
		this(authority.getId(), authority.getGym().getName());
	}
}
