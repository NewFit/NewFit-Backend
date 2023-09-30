package com.newfit.reservation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domain.reservation.Reservation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationDetailResponse extends ReservationResponse {
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public ReservationDetailResponse(Reservation reservation) {
        super(reservation.getId());
        this.startAt = reservation.getStart_at();
        this.endAt = reservation.getEnd_at();
    }
}
