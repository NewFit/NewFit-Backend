package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.Authority;
import lombok.*;

@Getter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthorityGymResponse {

    private final Long authorityId;
    private final String gymName;

    public AuthorityGymResponse(Authority authority) {
        this(authority.getId(), authority.getGym().getName());
    }
}
