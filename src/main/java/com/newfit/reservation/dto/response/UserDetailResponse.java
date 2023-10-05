package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.Authority;
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
    private Long currentAuthorityId;
    private String currentGymName;
    private Integer authorityGymsCount;
    private List<AuthorityGymResponse> authorityGyms;

    @Builder
    private UserDetailResponse(String filePath,
                               String nickname,
                               Long totalCredit,
                               Long monthCredit,
                               Long currentAuthorityId,
                               String currentGymName,
                               List<AuthorityGymResponse> authorityGyms) {
        this.profileFilePath = filePath;
        this.nickname = nickname;
        this.totalCredit = totalCredit;
        this.thisMonthCredit = monthCredit;
        this.currentAuthorityId = currentAuthorityId;
        this.currentGymName = currentGymName;
        this.authorityGyms = authorityGyms;
        this.authorityGymsCount = authorityGyms.size();
    }

    public static UserDetailResponse createUserDetailResponse(User user, Long monthCredit, Authority authority
            , List<AuthorityGymResponse> authorityGyms) {

        return UserDetailResponse.builder()
                .filePath(user.getFilePath())
                .nickname(user.getNickname())
                .totalCredit(user.getBalance())
                .monthCredit(monthCredit)
                .currentAuthorityId(authority.getId())
                .currentGymName(authority.getGym().getName())
                .authorityGyms(authorityGyms)
                .build();
    }
}
