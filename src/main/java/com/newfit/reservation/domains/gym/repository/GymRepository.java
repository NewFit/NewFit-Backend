package com.newfit.reservation.domains.gym.repository;

import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long>, GymRepositoryCustom {
}
