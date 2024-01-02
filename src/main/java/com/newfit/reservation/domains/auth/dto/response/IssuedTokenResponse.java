package com.newfit.reservation.domains.auth.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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

	private List<IdInformationResponse> idInformations;

	@Builder(access = AccessLevel.PRIVATE)
	private IssuedTokenResponse(String accessToken, String refreshToken, List<IdInformationResponse> idInformations) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.idInformations = idInformations;
	}

	public static IssuedTokenResponse registeredUser(String accessToken, String refreshToken,
		List<IdInformationResponse> idInformations) {
		return IssuedTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.idInformations(idInformations)
			.build();
	}

	public static IssuedTokenResponse unregisteredUser(String accessToken, IdInformationResponse idInformation) {
		return IssuedTokenResponse.builder()
			.accessToken(accessToken)
			.idInformations(List.of(idInformation))
			.build();
	}
}
