package com.newfit.reservation.domains.authority.dto.response.manager;

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

    @Builder(access = AccessLevel.PRIVATE)
    private UserAndPendingListResponse(String gymName,
                                       List<UserAndPendingResponse> requests,
                                       List<UserAndPendingResponse> users) {
        this.gymName = gymName;
        this.requests = requests;
        this.requestCount = requests.size();
        this.users = users;
        this.userCount = users.size();
    }

    public static UserAndPendingListResponse createResponse(String gymName,
                                                            List<UserAndPendingResponse> requests,
                                                            List<UserAndPendingResponse> users) {
        return UserAndPendingListResponse.builder()
                .gymName(gymName)
                .requests(requests)
                .users(users)
                .build();
    }
}
