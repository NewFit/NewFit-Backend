package com.newfit.reservation.domains.dev.repository;

import com.newfit.reservation.domains.dev.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
