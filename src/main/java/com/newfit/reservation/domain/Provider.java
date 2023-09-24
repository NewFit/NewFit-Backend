package com.newfit.reservation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    GOOGLE("google"), KAKAO("kakao");

    private final String description;
    public static Provider getProvider(String description) {
        if (description.equals(Provider.GOOGLE.description)) {
            return Provider.GOOGLE;
        }
        return Provider.KAKAO;
    }
}
