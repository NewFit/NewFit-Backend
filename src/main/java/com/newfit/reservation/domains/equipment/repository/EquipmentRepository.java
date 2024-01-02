package com.newfit.reservation.domains.equipment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.gym.domain.Gym;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
	// Gym, Name, Purpose가 모두 동일한 Equipment를 조회
	Optional<Equipment> findByGymAndNameAndPurposeType(Gym gym, String name, PurposeType purposeType);
}
