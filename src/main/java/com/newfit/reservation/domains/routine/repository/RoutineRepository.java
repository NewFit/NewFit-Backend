package com.newfit.reservation.domains.routine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.routine.domain.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

	Optional<Routine> findByAuthorityAndName(Authority authority, String name);

	List<Routine> findAllByAuthority(Authority authority);
}
