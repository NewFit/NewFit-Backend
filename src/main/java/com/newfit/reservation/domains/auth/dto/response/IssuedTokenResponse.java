package com.newfit.reservation.domains.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.auth.domain.IdType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IssuedTokenResponse {
	private String accessToken;
	private String refreshToken;

	private Long id;
	private String idType;

	@Builder(access = AccessLevel.PRIVATE)
	private IssuedTokenResponse(String accessToken, String refreshToken, Long id, String idType) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.idType = idType;
	}

	public static IssuedTokenResponse registeredUser(String accessToken, String refreshToken, Long id, IdType idType) {
		return IssuedTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.id(id)
			.idType(idType.getDescription())
			.build();
	}

	public static IssuedTokenResponse unregisteredUser(String accessToken, Long id, IdType idType) {
		return IssuedTokenResponse.builder()
			.accessToken(accessToken)
			.id(id)
			.idType(idType.getDescription())
			.build();
	}
}
