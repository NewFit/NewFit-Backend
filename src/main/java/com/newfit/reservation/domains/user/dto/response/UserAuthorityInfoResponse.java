package com.newfit.reservation.domains.user.dto.response;

import java.util.List;

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
public class UserAuthorityInfoResponse extends UserInfoResponse {

	private AuthorityGymResponse current;
	private Integer authorityGymsCount;
	private List<AuthorityGymResponse> authorityGyms;

	@Builder(access = AccessLevel.PRIVATE)
	private UserAuthorityInfoResponse(String filePath,
		String nickname,
		Long totalCredit,
		Long monthCredit,
		AuthorityGymResponse current,
		List<AuthorityGymResponse> authorityGyms) {
		super(filePath, nickname, totalCredit, monthCredit);
		this.current = current;
		this.authorityGyms = authorityGyms;
		this.authorityGymsCount = authorityGyms.size();
	}

	public static UserAuthorityInfoResponse createResponse(User user,
		Long monthCredit,
		AuthorityGymResponse current,
		List<AuthorityGymResponse> authorityGyms) {
		return UserAuthorityInfoResponse.builder()
			.filePath(user.getFilePath())
			.nickname(user.getNickname())
			.totalCredit(user.getBalance())
			.monthCredit(monthCredit)
			.current(current)
			.authorityGyms(authorityGyms)
			.build();
	}
}
