package com.newfit.reservation.domain.auth;

import com.newfit.reservation.domain.Provider;
import com.newfit.reservation.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String attributeName;

    private Boolean signup;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private OAuthHistory(Provider provider, String attributeName) {
        this.provider = provider;
        this.attributeName = attributeName;
        this.signup = false;
        this.user = null;
    }

    public static OAuthHistory createOAuthHistory(Provider provider, String attributeName) {
        return OAuthHistory.builder()
                .provider(provider)
                .attributeName(attributeName)
                .build();
    }

    public Long getId() {
        return id;
    }

    public Provider getProvider() {
        return provider;
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
}
