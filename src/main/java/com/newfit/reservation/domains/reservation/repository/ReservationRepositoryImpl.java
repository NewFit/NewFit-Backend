package com.newfit.reservation.domains.reservation.repository;

import static com.newfit.reservation.domains.reservation.domain.QReservation.*;

import java.util.List;

import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Reservation> findAllByAuthorityId(Long authorityId) {
		return queryFactory.selectFrom(reservation)
			.where(reservation.authority.id.eq(authorityId))
			.fetch();
	}
}
