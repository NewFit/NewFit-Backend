package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // AccessLevel을 PROTECTED로 제한합니다.
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAndPendingListResponse {

    private String gymName;
    private int requestCount;
    private List<UserAndPendingResponse> requests;
    private int userCount;
    private List<UserAndPendingResponse> users;

    @Builder    // 생성자를 private으로 설정하고 빌더 패턴 적용함으로써 오직 빌더로만 객체 생성 가능합니다.
    private UserAndPendingListResponse(String gymName,
                                       List<UserAndPendingResponse> requests,
                                       List<UserAndPendingResponse> users) {
        this.gymName = gymName;
        this.requests = requests;
        this.requestCount = requests.size();
        this.users = users;
        this.userCount = users.size();
    }
}
