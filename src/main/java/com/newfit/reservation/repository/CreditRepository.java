package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Credit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreditRepository {

    private final EntityManager em;

    // Credit 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(Credit credit) {
        em.persist(credit);
        return credit.getId();
    }

    // update 메소드 및 조회 메소드는 추가할 예정입니다.
}
