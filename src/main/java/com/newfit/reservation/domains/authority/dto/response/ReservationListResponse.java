package com.newfit.reservation.domains.authority.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationListResponse {

    private String gymName;
    private int reservationCount;
    private List<ReservationDetailResponse> reservations;

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationListResponse(String gymName,
                                    List<ReservationDetailResponse> reservationResponseList) {
        this.gymName = gymName;
        this.reservationCount = reservationResponseList.size();
        this.reservations = reservationResponseList;
    }

    public static ReservationListResponse createResponse(String gymName,
                                                         List<ReservationDetailResponse> reservationResponseList) {
        return ReservationListResponse.builder()
                .gymName(gymName)
                .reservationResponseList(reservationResponseList)
                .build();
    }
}
