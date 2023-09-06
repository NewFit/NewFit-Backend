package com.newfit.reservation.repository;

import com.newfit.reservation.domain.routine.EquipmentRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRoutineRepository extends JpaRepository<EquipmentRoutine, Long> {
}
