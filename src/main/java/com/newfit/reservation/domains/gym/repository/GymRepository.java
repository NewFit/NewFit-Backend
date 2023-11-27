package com.newfit.reservation.domains.gym.repository;

import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Long> {
    @Query(value = "select * from gym where name ~* :keywordString", nativeQuery = true)
    List<Gym> findAllByNameContaining(@Param("keywordString") String keywordString);
}
