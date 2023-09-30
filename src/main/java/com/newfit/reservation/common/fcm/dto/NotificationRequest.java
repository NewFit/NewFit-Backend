package com.newfit.reservation.common.fcm.dto;

import com.newfit.reservation.domain.reservation.Reservation;
import lombok.Getter;

@Getter
public class NotificationRequest {
    private Long userId;
    private String title;
    private String body;

    public NotificationRequest(Reservation reservation, String title) {
        this.userId = reservation.getReserver().getUser().getId();
        this.title = title;
        this.body = reservation.getEquipmentGym().getName() + " 사용 5분 전 입니다.";
    }
}
