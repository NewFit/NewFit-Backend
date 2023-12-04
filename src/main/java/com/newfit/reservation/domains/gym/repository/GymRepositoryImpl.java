package com.newfit.reservation.domains.gym.repository;

import com.newfit.reservation.domains.gym.domain.Gym;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.newfit.reservation.domains.gym.domain.QGym.*;

@RequiredArgsConstructor
public class GymRepositoryImpl implements GymRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Gym> findAllByNameContaining(List<String> keywords) {
        return queryFactory
                .selectFrom(gym)
                .where(containsKeyword(keywords))
                .fetch();
    }

    private BooleanBuilder containsKeyword(List<String> keywords) {
        return keywords.stream()
                .map(keyword -> gym.name.toLowerCase().contains(keyword.toLowerCase()))
                .reduce(new BooleanBuilder(), BooleanBuilder::or, BooleanBuilder::or);
    }
}
