package com.newfit.reservation.repository.location;


import com.newfit.reservation.domain.location.SiggArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiggAreaRepository extends JpaRepository<SiggArea, Long> {
}
