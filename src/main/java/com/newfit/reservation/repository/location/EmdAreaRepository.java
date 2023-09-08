package com.newfit.reservation.repository.location;

import com.newfit.reservation.domain.location.EmdArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmdAreaRepository extends JpaRepository<EmdArea, Long> {
}
