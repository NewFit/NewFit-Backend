package com.newfit.reservation.domains.authority.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), MANAGER("ROLE_MANAGER"), GUEST("ROLE_GUEST");

    private final String description;
}
