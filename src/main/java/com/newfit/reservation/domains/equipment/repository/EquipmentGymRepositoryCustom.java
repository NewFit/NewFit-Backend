package com.newfit.reservation.domains.equipment.repository;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import java.util.List;

public interface EquipmentGymRepositoryCustom {
    List<EquipmentGym> findAllByGymAndPurpose(Long gymId, PurposeType purpose);
}
