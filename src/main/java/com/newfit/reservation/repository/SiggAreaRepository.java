package com.newfit.reservation.repository;

import com.newfit.reservation.domain.SiggArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SiggAreaRepository {
    private final EntityManager entityManager;

    public void save(SiggArea siggArea) {
        if (siggArea.getId() == null) {
            entityManager.persist(siggArea);
        }
        else {
            entityManager.merge(siggArea);
        }
    }
}
