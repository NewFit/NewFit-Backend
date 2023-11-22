package com.newfit.reservation.domains.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.common.validator.EnumValue;
import com.newfit.reservation.domains.auth.domain.ProviderType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IssueTokenRequest {

    @EnumValue(value = ProviderType.class)
    private ProviderType providerType;

    @NotNull
    private String attributeName;
}