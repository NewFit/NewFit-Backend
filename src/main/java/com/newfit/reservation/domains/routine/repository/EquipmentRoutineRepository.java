package com.newfit.reservation.domains.routine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.routine.domain.EquipmentRoutine;
import com.newfit.reservation.domains.routine.domain.Routine;

public interface EquipmentRoutineRepository extends JpaRepository<EquipmentRoutine, Long> {

	// Routine의 모든 EquipmentRoutine 객체를 조회
	List<EquipmentRoutine> findAllByRoutine(Routine routine);

	// sequence로 정렬된 Routine의 모든 EquipmentRoutine 객체를 조회
	List<EquipmentRoutine> findAllByRoutineIdOrderBySequence(Long routineId);

	List<EquipmentRoutine> findAllByEquipment_Id(Long equipmentId);
}
