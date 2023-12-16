package com.newfit.reservation.domains.authority.repository;

import java.util.List;
import java.util.Optional;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;

public interface AuthorityRepositoryCustom {

	List<Authority> findAllAuthorityByUserId(Long id);

	Optional<Authority> findOneByUserIdAndGymIdAndRoleType(Long userId, Long gymId, RoleType roleType);

	List<Authority> findAllByCreditAcquisitionCountNotZero();

	Optional<Authority> findOneByUserNicknameAndGymId(String nickname, Long gymId);
}
