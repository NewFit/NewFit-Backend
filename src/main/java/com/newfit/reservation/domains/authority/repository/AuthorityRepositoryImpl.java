package com.newfit.reservation.domains.authority.repository;

import static com.newfit.reservation.domains.authority.domain.QAuthority.*;

import java.util.List;
import java.util.Optional;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Authority> findAllAuthorityByUserId(Long userId) {
		return queryFactory
			.selectFrom(authority)
			.where(authority.user.id.eq(userId))
			.fetch();
	}

	@Override
	public Optional<Authority> findOneByUserIdAndGymIdAndRoleType(Long userId, Long gymId, RoleType roleType) {
		return Optional.ofNullable(queryFactory
			.selectFrom(authority)
			.where(authority.user.id.eq(userId)
				.and(authority.gym.id.eq(gymId))
				.and(authority.roleType.eq(roleType)))
			.fetchOne());
	}

	@Override
	public List<Authority> findAllByCreditAcquisitionCountNotZero() {
		return queryFactory
			.selectFrom(authority)
			.where(authority.creditAcquisitionCount.eq((short)0).not())
			.fetch();
	}

	@Override
	public Optional<Authority> findOneByUserNicknameAndGymId(String nickname, Long gymId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(authority)
				.where(authority.user.nickname.eq(nickname)
					.and(authority.gym.id.eq(gymId)))
				.fetchOne()
		);
	}
}
