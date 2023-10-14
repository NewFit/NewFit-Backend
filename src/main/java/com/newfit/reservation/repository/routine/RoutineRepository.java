package com.newfit.reservation.repository.routine;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Optional<Routine> findByAuthorityAndName(Authority authority, String name);

    List<Routine> findAllByAuthority(Authority authority);

    void deleteAllByAuthority(Authority authority);
}
