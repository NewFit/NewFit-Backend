package com.newfit.reservation.domains.gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public class BusinessTime {

    // 24시간 운영이면 openAt과 closeAt을 모두 00:00으로 설정
    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column(nullable = false)
    private Boolean allDay;

    // getAllDay가 아닌 isAllDay라는 이름을 사용하기 위해 @Getter 미사용
    public boolean isAllDay() {
        return this.allDay;
    }
    
    public LocalTime getOpenAt() {
        return openAt;
    }

    public LocalTime getCloseAt() {
        return closeAt;
    }
}
