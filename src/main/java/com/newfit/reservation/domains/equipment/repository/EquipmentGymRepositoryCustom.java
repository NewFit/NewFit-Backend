package com.newfit.reservation.domains.equipment.repository;

import java.util.List;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;

public interface EquipmentGymRepositoryCustom {
	List<EquipmentGym> findAllByGymAndPurpose(Long gymId, PurposeType purpose);
}
