package com.newfit.reservation.domains.gym.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.gym.domain.Gym;
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

    @Builder(access = AccessLevel.PRIVATE)
    private GymResponse(Long gymId, String gymName, String address) {
        this.gymId = gymId;
        this.gymName = gymName;
        this.address = address;
    }

    // Gym 객체로부터 GymResponseDto를 생성합니다.
    public GymResponse(Gym gym) {
        this(gym.getId(), gym.getName(), gym.getAddress());
    }
}
