package com.newfit.reservation.domains.auth.domain;

import com.newfit.reservation.domains.user.domain.User;
import jakarta.persistence.*;
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
    private Provider provider;

    // Google의 경우 sub값, Kakao의 경우 id값을 담는 필드
    @Column(nullable = false)
    private String attributeName;

    private Boolean signup;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @Builder(access = AccessLevel.PRIVATE)
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

    public void signUp(User user) {
        this.signup = true;
        this.user = user;
    }
}
