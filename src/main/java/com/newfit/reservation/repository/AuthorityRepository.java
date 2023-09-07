package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Authority;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorityRepository {

    private final EntityManager em;

    // Authority 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(Authority authority) {
        em.persist(authority);
        return authority.getId();
    }

    // DB에서 Authority 엔티티 객체의 id를 통해 조회하는 메소드입니다.
    public Optional<Authority> findOne(Long authorityId) {
        return Optional.ofNullable(em.find(Authority.class, authorityId));
    }

    // 특정 Gym을 이용하는 모든 사용자들의 Authority 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    // Authority를 조회하는 경우 그에 묶인 User 도 같이 필요로 할 확률이 높아서 fetch join 방식을 사용했습니다.
    public List<Authority> findAuthoritiesByGym(Long gymId) {
        return em.createQuery("select a from Authority a join fetch a.user u where a.gym.id = :gymId",
                        Authority.class)
                .setParameter("gymId", gymId)
                .getResultList();
    }
}
