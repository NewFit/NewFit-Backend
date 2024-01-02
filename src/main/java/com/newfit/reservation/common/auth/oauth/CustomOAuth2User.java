package com.newfit.reservation.common.auth.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.newfit.reservation.domains.auth.domain.OAuthHistory;

import lombok.Getter;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {   // OAuth2 인증이 완료된 사용자 정보를 담는 클래스
	private OAuthHistory oAuthHistory;

	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
		String nameAttributeKey, OAuthHistory oAuthHistory) {
		super(authorities, attributes, nameAttributeKey);
		this.oAuthHistory = oAuthHistory;
	}
}
