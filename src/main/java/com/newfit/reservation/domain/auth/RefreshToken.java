package com.newfit.reservation.domain.auth;

import com.newfit.reservation.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, name = "refresh_token")
    private String refreshToken;

    @Builder
    private RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public static RefreshToken createRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
    }
}
