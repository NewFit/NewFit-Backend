package com.newfit.reservation.repository.dev;

import com.newfit.reservation.domain.dev.Report;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final EntityManager em;

    // Report 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(Report report) {
        em.persist(report);
        return report.getId();
    }

    // DB 에서 Report 엔티티 객체의 id 를 통해 조회하는 메소드입니다.
    public Optional<Report> findOne(Long reportId) {
        return Optional.ofNullable(em.find(Report.class, reportId));
    }

    // DB 에서 모든 Report 엔티티 객체를 리스트 형식으로 조회하는 메소드입니다.
    public List<Report> findAll() {
        return em.createQuery("select r from Report r", Report.class)
                .getResultList();
    }
}
