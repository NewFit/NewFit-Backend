package com.newfit.reservation.repository.dev;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.dev.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUser(User user);
}
