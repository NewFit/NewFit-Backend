package com.newfit.reservation.repository;

import com.newfit.reservation.domain.location.SidoArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SidoAreaRepository {
    private final EntityManager entityManager;

    public void save(SidoArea sidoArea) {
        if (sidoArea.getId() == null) {
            entityManager.persist(sidoArea);
        }
        else {
            entityManager.merge(sidoArea);
        }
    }
}
