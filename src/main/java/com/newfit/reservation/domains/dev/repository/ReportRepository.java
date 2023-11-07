package com.newfit.reservation.domains.dev.repository;

import com.newfit.reservation.domains.dev.domain.Report;
import com.newfit.reservation.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByUser(User user);
}
