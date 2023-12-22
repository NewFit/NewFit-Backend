package com.newfit.reservation.domains.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.auth.domain.IdType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdInformationResponse {
	private final Long id;
	private final String idType;

	@Builder(access = AccessLevel.PRIVATE)
	private IdInformationResponse(Long id, String idType) {
		this.id = id;
		this.idType = idType;
	}

	public static IdInformationResponse createResponse(Long id, IdType idType) {
		return IdInformationResponse.builder()
			.id(id)
			.idType(idType.getDescription())
			.build();
	}
}
