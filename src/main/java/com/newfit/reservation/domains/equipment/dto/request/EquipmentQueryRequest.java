package com.newfit.reservation.domains.equipment.dto.request;

import com.newfit.reservation.domains.equipment.domain.PurposeType;

public record EquipmentQueryRequest(PurposeType purposeType, Long equipmentId) {
}
