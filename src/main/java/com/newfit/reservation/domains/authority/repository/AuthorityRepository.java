package com.newfit.reservation.domains.authority.repository;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, AuthorityRepositoryCustom {
    List<Authority> findAllAuthorityByGym(Gym gym);
}
