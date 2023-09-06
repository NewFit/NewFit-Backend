package com.newfit.reservation.repository;

import com.newfit.reservation.domain.location.SidoArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoAreaRepository extends JpaRepository<SidoArea, Long> {
}
