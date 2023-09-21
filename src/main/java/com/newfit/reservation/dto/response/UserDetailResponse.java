package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponse {

    private String profileFilePath;

    private String nickname;

    private Long totalCredit;

    private Long thisMonthCredit;

    @Builder
    private UserDetailResponse(String filePath,
                               String nickname,
                               Long totalCredit,
                               Long monthCredit) {
        this.profileFilePath = filePath;
        this.nickname = nickname;
        this.totalCredit = totalCredit;
        this.thisMonthCredit = monthCredit;
    }
}
