package com.newfit.reservation.domains.gym.repository;

import java.util.List;

import com.newfit.reservation.domains.gym.domain.Gym;

public interface GymRepositoryCustom {

	List<Gym> findAllByNameContaining(List<String> keywordString);
}
