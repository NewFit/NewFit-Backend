package com.newfit.reservation.common.auth.oauth;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.auth.domain.ProviderType;
import com.newfit.reservation.domains.auth.repository.OAuthHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
	private final OAuthHistoryRepository oAuthHistoryRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		ProviderType providerType = ProviderType.getProviderType(
			userRequest.getClientRegistration().getRegistrationId());
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String nameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		// OAuth2 인증을 통해 얻어온 사용자 정보로 OAuthHistory 객체 생성
		OAuthHistory oAuthHistory = findOAuthHistory(providerType, String.valueOf(attributes.get(nameAttributeName)));
		return new CustomOAuth2User(null, attributes, nameAttributeName, oAuthHistory);
	}

	private OAuthHistory findOAuthHistory(ProviderType providerType, String attributeName) {
		Optional<OAuthHistory> oAuthHistory = oAuthHistoryRepository.findByProviderTypeAndAttributeName(providerType,
			attributeName);
		return oAuthHistory.orElseGet(
			() -> oAuthHistoryRepository.save(OAuthHistory.createOAuthHistory(providerType, attributeName)));
	}
}
