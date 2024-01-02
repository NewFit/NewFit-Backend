package com.newfit.reservation.domains.auth.domain;

import com.newfit.reservation.domains.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthHistory { // OAuth2 인증을 통해 얻어온 사용자 정보를 담는 클래스
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProviderType providerType;

	// Google의 경우 sub값, Kakao의 경우 id값을 담는 필드
	@Column(nullable = false)
	private String attributeName;

	private Boolean signup;

	@JoinColumn(name = "user_id")
	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	@Builder(access = AccessLevel.PRIVATE)
	private OAuthHistory(ProviderType providerType, String attributeName) {
		this.providerType = providerType;
		this.attributeName = attributeName;
		this.signup = false;
		this.user = null;
	}

	public static OAuthHistory createOAuthHistory(ProviderType providerType, String attributeName) {
		return OAuthHistory.builder()
			.providerType(providerType)
			.attributeName(attributeName)
			.build();
	}

	public Long getId() {
		return id;
	}

	public ProviderType getProviderType() {
		return providerType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public Boolean isRegistered() {
		return signup;
	}

	public User getUser() {
		return user;
	}

	public void signUp(User user) {
		this.signup = true;
		this.user = user;
	}
}
