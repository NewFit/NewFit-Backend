package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Authority;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthorityRepository {

    private final EntityManager em;

    // Authority 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(Authority authority) {
        em.persist(authority);
        return authority.getId();
    }

    // 조회 메소드는 추가할 예정입니다.
}
