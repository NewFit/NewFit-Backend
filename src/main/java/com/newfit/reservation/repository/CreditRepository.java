package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Credit;
import jakarta.persistence.EntityManager;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CreditRepository {

    private final EntityManager em;

    // Credit 엔티티 객체를 DB에 저장하는 메소드입니다.
    public Long save(Credit credit) {
        em.persist(credit);
        return credit.getId();
    }
  
    // DB에서 Credit 엔티티 객체의 id를 통해 조회하는 메소드입니다.
    public Optional<Credit> findOne(Long creditId) {
        return Optional.ofNullable(em.find(Credit.class, creditId));
    }

    // 특정 Gym을 이용하는 모든 사용자들의 모든 Credit 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    // 크레딧의 획득 연도나 획득 월에 상관없이 모두 조회합니다.
    public List<Credit> findCreditsByGym(Long gymId) {
        return em.createQuery("select c from Credit c where c.authority.gym.id = :gymId",
                        Credit.class)
                .setParameter("gymId", gymId)
                .getResultList();
    }

    // 특정 Gym을 이용하는 모든 사용자들의 Credit 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    // 다만, 이 메소드에서는 year와 month정보를 추가로 받아서 특정 year, 특정 month에 획득한 Credit 엔티티 객체들을 조회합니다.
    public List<Credit> findCreditsByGymAndYearAndMonth(Long gymId, short year, short month) {
        return em.createQuery("select c from Credit c where c.authority.gym.id = :gymId" +
                                " and c.year = :year and c.month = :month", Credit.class)
                .setParameter("gymId", gymId)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    // '특정 사용자'의 Credit 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    /* 다만, User의 id로 조회하는 것이 아닌 Authority의 id로 조회하기 때문에
    해당 Authority에 묶인 Gym에서 획득한 Credit만을 조회합니다. 사용자가 특정 연도에
    획득한 크레딧을(1 ~ 12월) 모두 조회하는 경우에 사용됩니다.
     */
    public List<Credit> findCreditsByAuthorityAndYear(Long authorityId, short year) {
        return em.createQuery("select c from Credit c where c.authority.id = :authorityId" +
                        " and c.year = :year", Credit.class)
                .setParameter("authorityId", authorityId)
                .setParameter("year", year)
                .getResultList();
    }

    // '특정 사용자'의 Credit 엔티티 객체를 조회하는 메소드입니다.
    /* 다만, User의 id로 조회하는 것이 아닌 Authority의 id로 조회하기 때문에
    해당 Authority에 묶인 Gym에서 획득한 Credit만을 조회합니다. 사용자가 특정 연도,
    특정 월에 획득한 크레딧을 조회하는 경우에 사용됩니다.
     */
    public Optional<Credit> findCreditByAuthorityAndYearAndMonth(Long authorityId, short year, short month) {

        try {
            return Optional.of(em.createQuery("select c from Credit c where c.authority.id = :authorityId " +
                            "and c.year = :year and c.month = :month", Credit.class)
                    .setParameter("authorityId", authorityId)
                    .setParameter("year", year)
                    .setParameter("month", month)
                    .getSingleResult());
        } catch(NoResultException e) {
            return Optional.empty();
        }
    }

    // update 메소드는 논의가 더 필요합니다.
}
