package com.newfit.reservation.service;

import com.newfit.reservation.domain.Report;
import com.newfit.reservation.dto.request.CreateReportRequestDto;

import com.newfit.reservation.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {        // ReportRepository 에 단순 위임하는 Service 클래스입니다.

    private final ReportRepository reportRepository;

    public Long saveReport(CreateReportRequestDto reportRequestDto) {

        // CreateReportRequestDto 객체를 Report 엔티티 객체로 변환하는 메소드 사용했습니다.
        Report report = CreateReportRequestDto.reportDto2Entity(reportRequestDto);
        return reportRepository.save(report);
    }

    public Optional<Report> findOneById(Long reportId) {
        return reportRepository.findOne(reportId);
    }

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }
}
