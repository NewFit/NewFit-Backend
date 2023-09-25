package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRankInfoListResponse {
    private final String gymName;
    private final Long rankingsCount;
    private final List<UserRankInfo> rankings;

    public UserRankInfoListResponse(String gymName, List<UserRankInfo> rankings) {
        this.gymName = gymName;
        this.rankingsCount = (long) rankings.size();
        this.rankings = rankings;

    }
}
