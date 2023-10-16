package com.newfit.reservation.domains.routine.repository;

import com.newfit.reservation.domains.routine.domain.EquipmentRoutine;
import com.newfit.reservation.domains.routine.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentRoutineRepository extends JpaRepository<EquipmentRoutine, Long> {

    // Routine의 모든 EquipmentRoutine 객체를 삭제
    void deleteAllByRoutine(Routine routine);

    // Routine의 모든 EquipmentRoutine 객체를 조회
    List<EquipmentRoutine> findAllByRoutine(Routine routine);

    // EquipmentId와 Routine으로 단일 EquipmentRoutine 객체를 조회
    Optional<EquipmentRoutine> findByEquipmentIdAndRoutine(Long equipmentId, Routine routine);

    // sequence로 정렬된 Routine의 모든 EquipmentRoutine 객체를 조회
    List<EquipmentRoutine> findAllByRoutineIdOrderBySequence(Long routineId);
}
