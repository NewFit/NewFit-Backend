package com.newfit.reservation.domains.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.dev.domain.Report;
import com.newfit.reservation.domains.user.domain.User;

public interface ReportRepository extends JpaRepository<Report, Long> {

	List<Report> findAllByUser(User user);
}
