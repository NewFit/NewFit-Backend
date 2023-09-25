package com.newfit.reservation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("user"), MANAGER("manager"), GUEST("guest");

    private final String description;
}
