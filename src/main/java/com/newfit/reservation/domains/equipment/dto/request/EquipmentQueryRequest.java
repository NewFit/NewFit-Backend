package com.newfit.reservation.domains.equipment.dto.request;

import com.newfit.reservation.domains.equipment.domain.PurposeType;

public record EquipmentQueryRequest(PurposeType purpose, Long equipment_id) {
}
