package com.newfit.reservation.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // AccessLevel을 PROTECTED로 제한합니다.
public class UserAcceptResponse {

    private String username;

    @Builder    // 생성자를 private으로 설정하고 빌더 패턴 적용함으로써 오직 빌더로만 객체 생성 가능합니다.
    private UserAcceptResponse(String username) {
        this.username = username;
    }
}
