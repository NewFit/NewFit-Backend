package com.newfit.reservation.domain.common;

import com.newfit.reservation.domain.User;
import jakarta.persistence.*;
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

    @Builder
    private FCMToken(User user, String fcmToken) {
        this.user = user;
        this.fcmToken = fcmToken;
    }
}
