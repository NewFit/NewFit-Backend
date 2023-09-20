package com.newfit.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public class BusinessTime {

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
