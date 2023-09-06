package com.newfit.reservation.repository;

import com.newfit.reservation.domain.location.EmdArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmdAreaRepository extends JpaRepository<EmdArea, Long> {
}
