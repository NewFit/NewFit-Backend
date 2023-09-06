package com.newfit.reservation.repository;

import com.newfit.reservation.domain.location.EmdArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmdAreaRepository {
    private final EntityManager entityManager;

    public void save(EmdArea emdArea) {
        if (emdArea.getId() == null) {
            entityManager.persist(emdArea);
        }
        else {
            entityManager.merge(emdArea);
        }
    }
}
