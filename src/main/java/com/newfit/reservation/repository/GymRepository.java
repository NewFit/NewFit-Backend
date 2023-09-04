package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Gym;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GymRepository {
    private final EntityManager entityManager;

    public Long save(Gym gym) {
        if (gym.getId() == null) {
            entityManager.persist(gym);
        }
        else {
            entityManager.merge(gym);
        }
        return gym.getId();
    }
}
