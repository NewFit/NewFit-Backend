package com.newfit.reservation.repository;

import com.newfit.reservation.domain.location.SiggArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiggAreaRepository extends JpaRepository<SiggArea, Long> {
}
