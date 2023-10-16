package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.User;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponse {

    private String profileFilePath;
    private String nickname;
    private Long totalCredit;
    private Long thisMonthCredit;
    private AuthorityGymResponse current;
    private Integer authorityGymsCount;
    private List<AuthorityGymResponse> authorityGyms;

    @Builder(access = AccessLevel.PRIVATE)
    private UserDetailResponse(String filePath,
                               String nickname,
                               Long totalCredit,
                               Long monthCredit,
                               AuthorityGymResponse current,
                               List<AuthorityGymResponse> authorityGyms) {
        this.profileFilePath = filePath;
        this.nickname = nickname;
        this.totalCredit = totalCredit;
        this.thisMonthCredit = monthCredit;
        this.current = current;
        this.authorityGyms = authorityGyms;
        this.authorityGymsCount = authorityGyms.size();
    }

    public static UserDetailResponse createUserDetailResponse(User user,
                                                              Long monthCredit,
                                                              AuthorityGymResponse current,
                                                              List<AuthorityGymResponse> authorityGyms) {
        return UserDetailResponse.builder()
                .filePath(user.getFilePath())
                .nickname(user.getNickname())
                .totalCredit(user.getBalance())
                .monthCredit(monthCredit)
                .current(current)
                .authorityGyms(authorityGyms)
                .build();
    }
}
