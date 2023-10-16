package com.newfit.reservation.domains.auth.domain;

import com.newfit.reservation.domains.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {
    @Id
    private Long id;
    @Indexed
    private String token;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static RefreshToken createRefreshToken(User user, String token) {
        return RefreshToken.builder()
                .id(user.getId())
                .token(token)
                .build();
    }
}
