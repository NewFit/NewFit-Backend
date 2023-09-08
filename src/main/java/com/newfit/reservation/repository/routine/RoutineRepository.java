package com.newfit.reservation.repository.routine;

import com.newfit.reservation.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
