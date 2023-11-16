package com.newfit.reservation.domains.gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessTime {

    // 24시간 운영이면 openAt과 closeAt을 모두 00:00으로 설정
    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column(nullable = false)
    private Boolean allDay;

    @Builder(access = AccessLevel.PRIVATE)
    private BusinessTime(LocalTime openAt, LocalTime closeAt, Boolean allDay) {
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.allDay = allDay;
    }

    public static BusinessTime create(LocalTime openAt, LocalTime closeAt, Boolean allDay) {
        return BusinessTime.builder()
                .openAt(openAt)
                .closeAt(closeAt)
                .allDay(allDay)
                .build();
    }

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

    public void updateOpenAt(LocalTime openAt) {
        if (openAt != null) {
            this.openAt = openAt;
        }
    }

    public void updateCloseAt(LocalTime closeAt) {
        if (closeAt != null) {
            this.closeAt = closeAt;
        }
    }

    public void updateAllDay(Boolean allDay) {
        if (allDay != null) {
            this.allDay = allDay;
        }
    }
}
