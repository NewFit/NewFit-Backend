package com.newfit.reservation.domains.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoResponse {

	private String profileFilePath;
	private String nickname;
	private Long totalCredit;
	private Long thisMonthCredit;

	@Builder(access = AccessLevel.PRIVATE)
	protected UserInfoResponse(String profileFilePath, String nickname,
		Long totalCredit, Long monthCredit) {
		this.profileFilePath = profileFilePath;
		this.nickname = nickname;
		this.totalCredit = totalCredit;
		this.thisMonthCredit = monthCredit;
	}

	public static UserInfoResponse createResponse(User user, Long monthCredit) {
		return UserInfoResponse.builder()
			.profileFilePath(user.getFilePath())
			.nickname(user.getNickname())
			.totalCredit(user.getBalance())
			.monthCredit(monthCredit)
			.build();
	}
}
