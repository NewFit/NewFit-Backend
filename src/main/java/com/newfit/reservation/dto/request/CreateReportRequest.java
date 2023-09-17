package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.dev.Report;
import lombok.Getter;

@Getter
public class CreateReportRequest {

    private String subject;
    private String content;

    // Dto -> Entity 변환 메소드입니다.
    public static Report reportDto2Entity(CreateReportRequest reportRequestDto) {
        return Report.builder()
                .subject(reportRequestDto.getSubject())
                .content(reportRequestDto.getContent())
                .build();
    }
}
