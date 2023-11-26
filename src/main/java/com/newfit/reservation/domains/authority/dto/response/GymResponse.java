package com.newfit.reservation.domains.authority.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.authority.domain.Authority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GymResponse {
    private Long gymId;
    private String gymName;
    private String address;

    @Builder
    private GymResponse(Long gymId, String gymName, String address) {
        this.gymId = gymId;
        this.gymName = gymName;
        this.address = address;
    }

    public GymResponse(Authority authority) {
        this(authority.getGym().getId(), authority.getGym().getName(), authority.getGym().getAddress());
    }
}
