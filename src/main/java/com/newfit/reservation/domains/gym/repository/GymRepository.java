package com.newfit.reservation.domains.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.gym.domain.Gym;

public interface GymRepository extends JpaRepository<Gym, Long>, GymRepositoryCustom {
}
