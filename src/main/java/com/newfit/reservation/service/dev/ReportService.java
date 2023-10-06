package com.newfit.reservation.service.dev;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.dev.Report;
import com.newfit.reservation.dto.request.CreateReportRequest;

import com.newfit.reservation.repository.dev.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {        // ReportRepository 에 단순 위임하는 Service 클래스입니다.

    private final ReportRepository reportRepository;

    public void saveReport(User user, CreateReportRequest request) {
        Report report = Report.createReport(user, request.getSubject(), request.getContent());
        reportRepository.save(report);
    }
}
