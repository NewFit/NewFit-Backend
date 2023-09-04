package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Proposal;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProposalRepository {

    private final EntityManager em;

    // Proposal 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(Proposal proposal) {
        em.persist(proposal);
        return proposal.getId();
    }

    // DB 에서 Proposal 엔티티 객체의 id 를 통해 조회하는 메소드입니다.
    public Optional<Proposal> findOne(Long proposalId) {
        return Optional.ofNullable(em.find(Proposal.class, proposalId));
    }

    // DB 에서 모든 Proposal 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    public List<Proposal> findAll() {
        return em.createQuery("select p from Proposal p", Proposal.class)
                .getResultList();
    }
}
