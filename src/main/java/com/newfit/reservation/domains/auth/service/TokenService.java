package com.newfit.reservation.domains.auth.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.domains.auth.domain.IdType;
import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.auth.domain.ProviderType;
import com.newfit.reservation.domains.auth.dto.request.IssueTokenRequest;
import com.newfit.reservation.domains.auth.dto.response.IdInformationResponse;
import com.newfit.reservation.domains.auth.dto.response.IssuedTokenResponse;
import com.newfit.reservation.domains.auth.repository.OAuthHistoryRepository;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
	private final TokenProvider tokenProvider;
	private final OAuthHistoryRepository oAuthHistoryRepository;

	public IssuedTokenResponse issueToken(IssueTokenRequest request) {
		OAuthHistory oAuthHistory = getOAuthHistory(request.getProviderType(), request.getAttributeName());

		if (!oAuthHistory.isRegistered()) {
			String accessToken = tokenProvider.generateAccessToken(oAuthHistory.getUser());
			log.info("BeforeSignup.accessToken = {}", accessToken);
			IdInformationResponse idInformation = IdInformationResponse.createResponse(oAuthHistory.getId(),
				IdType.OAUTH_HISTORY);
			return IssuedTokenResponse.unregisteredUser(accessToken, idInformation);
		}

		// 회원가입이 완료된 경우
		Authority authority = oAuthHistory.getUser().getAuthorityList().stream()
			.filter(Authority::getAccepted).findAny().orElse(null);
		return getIssuedTokenResponse(oAuthHistory.getUser(), authority);

	}

	private IssuedTokenResponse getIssuedTokenResponse(User user, Authority authority) {
		String accessToken = tokenProvider.generateAccessToken(user);
		String refreshToken = tokenProvider.generateRefreshToken(user);
		log.info("AfterSignup.accessToken = {}", accessToken);

		List<IdInformationResponse> idInformations = Arrays.asList(
			IdInformationResponse.createResponse(user.getId(), IdType.USER));

		if (authority != null) {    // 등록된 gym이 있는 경우
			idInformations.add(IdInformationResponse.createResponse(authority.getId(), IdType.AUTHORITY));
		}
		return IssuedTokenResponse.registeredUser(accessToken, refreshToken, idInformations);
	}

	private OAuthHistory getOAuthHistory(ProviderType providerType, String attributeName) {
		return oAuthHistoryRepository.findByProviderTypeAndAttributeName(providerType, attributeName)
			.orElseGet(() -> oAuthHistoryRepository.save(OAuthHistory.createOAuthHistory(providerType, attributeName)));
	}
}