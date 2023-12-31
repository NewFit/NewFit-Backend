package com.newfit.reservation.domains.fcm.domain;

import com.newfit.reservation.domains.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCMToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "user_id", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(name = "fcm_token")
	private String fcmToken;

	@Builder(access = AccessLevel.PRIVATE)
	private FCMToken(User user, String fcmToken) {
		this.user = user;
		this.fcmToken = fcmToken;
	}

	public static FCMToken createFCMToken(User user, String fcmToken) {
		return FCMToken.builder()
			.user(user)
			.fcmToken(fcmToken)
			.build();
	}
}
