package com.newfit.reservation.repository;

import com.newfit.reservation.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    // update 메소드 및 조회 메소드는 추가할 예정입니다.
}
