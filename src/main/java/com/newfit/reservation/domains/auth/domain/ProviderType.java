package com.newfit.reservation.domains.auth.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import java.util.stream.Stream;
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

    @JsonCreator(mode = Mode.DELEGATING)
    public static ProviderType findByString(String providerType) {
        return Stream.of(ProviderType.values())
                .filter(type -> type.name().equals(providerType))
                .findFirst()
                .orElse(null);
    }
}
