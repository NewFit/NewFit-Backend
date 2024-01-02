package com.newfit.reservation.domains.credit.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRankInfoListResponse {
	private final String gymName;
	private final Long rankingsCount;
	private final List<UserRankInfo> rankings;

	private final UserRankInfo userRankInfo;

	public UserRankInfoListResponse(String gymName, List<UserRankInfo> rankings, UserRankInfo userRankInfo) {
		this.gymName = gymName;
		this.rankingsCount = (long)rankings.size();
		this.rankings = rankings;
		this.userRankInfo = userRankInfo;
	}
}
