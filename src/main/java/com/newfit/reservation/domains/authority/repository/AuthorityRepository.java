package com.newfit.reservation.domains.authority.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import com.newfit.reservation.domains.gym.domain.Gym;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, AuthorityRepositoryCustom {
	List<Authority> findAllAuthorityByGym(Gym gym);

	Boolean existsByUser_IdAndGym_IdAndRoleType(Long userId, Long gymId, RoleType roleType);
}
