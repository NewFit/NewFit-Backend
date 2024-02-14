package com.newfit.reservation.domains.equipment.repository;

import java.util.List;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.dto.request.EquipmentQueryRequest;
import com.newfit.reservation.domains.gym.domain.Gym;

public interface EquipmentGymRepositoryCustom {
	List<EquipmentGym> findAllByQuery(Gym gym, EquipmentQueryRequest request);
}
