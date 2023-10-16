package com.newfit.reservation.domains.authority.dto.response.manager;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.authority.domain.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAndPendingResponse {

    // 모든 필드를 final로 설정했습니다.
    private final Long userId;
    private final String username;

    /*
    이 생성자 내부에서 @RequiredArgsConstructor를 통해 생성되는 생성자를 호출하도록 작성했습니다.
    필드값을 직접 매개변수로 넘겨받는 것이 아니므로 우선은 Builder 패턴은 적용하지 않았습니다.
     */
    public UserAndPendingResponse(Authority authority) {
        this(authority.getUser().getId(),authority.getUser().getUsername());
    }
}
