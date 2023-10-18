package com.newfit.reservation.domains.authority.repository;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.Role;
import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllAuthorityByGym(Gym gym);

    @Query("SELECT a FROM Authority a WHERE a.user.id = :userId")
    List<Authority> findAllAuthorityByUserId(@Param("userId") Long id);

    @Query("SELECT a from Authority a where a.user.id =:userId and a.gym.id =:gymId and a.role =:role")
    Authority findOneByUserIdAndGymIdAndRole(@Param("userId") Long userId, @Param("gymId") Long gymId, @Param("role") Role role);

    @Query("select a from Authority a where a.creditAcquisitionCount <> 0")
    List<Authority> findAllByCreditAcquisitionCountNotZero();
}
