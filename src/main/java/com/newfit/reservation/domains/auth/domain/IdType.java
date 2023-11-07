package com.newfit.reservation.domains.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdType {
    AUTHORITY("authority-id"),
    USER("user-id"),
    OAUTH_HISTORY("oauth-history-id");

    private final String description;
}
