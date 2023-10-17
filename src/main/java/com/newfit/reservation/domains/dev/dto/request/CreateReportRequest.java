package com.newfit.reservation.domains.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReportRequest {

    @NotBlank
    @Length(max = 30)
    private String subject;

    @NotBlank
    @Length(max = 300)
    private String content;
}
