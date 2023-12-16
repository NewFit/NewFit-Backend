package com.newfit.reservation.domains.reservation.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentInfoResponse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EquipmentGymsListResponse {
	private Long equipmentGymsCount;
	private List<EquipmentInfoResponse> equipmentGyms;

	@Builder(access = AccessLevel.PRIVATE)
	private EquipmentGymsListResponse(List<EquipmentInfoResponse> equipmentGyms) {
		this.equipmentGymsCount = (long)equipmentGyms.size();
		this.equipmentGyms = equipmentGyms;
	}

	public static EquipmentGymsListResponse createResponse(List<EquipmentInfoResponse> equipmentGyms) {
		return EquipmentGymsListResponse
			.builder()
			.equipmentGyms(equipmentGyms)
			.build();
	}
}
