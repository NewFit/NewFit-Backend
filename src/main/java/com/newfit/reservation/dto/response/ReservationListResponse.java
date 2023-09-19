package com.newfit.reservation.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationListResponse {

    private String gymName;
    private int reservationCount;
    private List<ReservationDetailResponse> reservations;

    @Builder
    public ReservationListResponse(String gymName,
                                   List<ReservationDetailResponse> reservationResponseList) {
        this.gymName = gymName;
        this.reservationCount = reservationResponseList.size();
        this.reservations = reservationResponseList;
    }

}
