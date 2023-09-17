package com.newfit.reservation.repository.routine;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Optional<Routine> findByAuthorityAndName(Authority authority, String name);

    List<Routine> findAllByAuthority(Authority authority);
}
