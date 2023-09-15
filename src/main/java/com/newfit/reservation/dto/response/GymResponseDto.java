package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GymResponseDto {
    private String gymName;
    private String address;

    @Builder
    private GymResponseDto(String gymName, String address) {
        this.gymName = gymName;
        this.address = address;
    }

    public GymResponseDto(Authority authority) {
        this(authority.getGym().getName(), authority.getGym().getAddress());
    }

    // Gym 객체로부터 GymResponseDto를 생성합니다.
    public GymResponseDto(Gym gym) { this(gym.getName(), gym.getAddress()); }
}
