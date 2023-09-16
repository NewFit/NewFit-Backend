package com.newfit.reservation.repository.routine;

import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRoutineRepository extends JpaRepository<EquipmentRoutine, Long> {

    // 특정 Routine에 묶여있는 모든 EquipmentRoutine 객체를 삭제하는 메소드입니다.
    void deleteAllByRoutine(Routine routine);
}
