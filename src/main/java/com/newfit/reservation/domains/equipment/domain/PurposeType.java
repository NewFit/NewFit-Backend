package com.newfit.reservation.domains.equipment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurposeType {
	AEROBIC("유산소"),
	ARM("팔"),
	BACK("등"),
	CHEST("가슴"),
	LOWER_BODY("하체");

	private final String description;
}
