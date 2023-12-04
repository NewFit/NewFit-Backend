package com.newfit.reservation.domains.gym.repository;

import com.newfit.reservation.domains.gym.domain.Gym;
import java.util.List;

public interface GymRepositoryCustom {

    List<Gym> findAllByNameContaining(List<String> keywordString);
}
