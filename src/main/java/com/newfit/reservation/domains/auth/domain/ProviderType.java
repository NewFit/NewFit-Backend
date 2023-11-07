package com.newfit.reservation.domains.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    GOOGLE("google"), KAKAO("kakao");

    private final String description;
    public static ProviderType getProviderType(String description) {
        if (description.equals(ProviderType.GOOGLE.description)) {
            return ProviderType.GOOGLE;
        }
        return ProviderType.KAKAO;
    }
}
