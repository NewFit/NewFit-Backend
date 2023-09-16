package com.newfit.reservation.repository.routine;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Optional<Routine> findByUserAndName(User user, String name);
}
