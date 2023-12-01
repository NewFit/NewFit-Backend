package com.newfit.reservation.domains.reservation.repository;

import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.newfit.reservation.domains.reservation.domain.QReservation.*;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> findAllByAuthorityId(Long authorityId) {
        return queryFactory.selectFrom(reservation)
                .where(reservation.authority.id.eq(authorityId))
                .fetch();
    }
}
