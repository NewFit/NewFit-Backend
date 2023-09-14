package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.Authority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
