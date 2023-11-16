package com.newfit.reservation.domains.gym.domain;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.gym.dto.request.admin.CreateGymRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.newfit.reservation.common.exception.ErrorCodeType.INVALID_RESERVATION_REQUEST;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String address;

    @Embedded
    private BusinessTime businessTime;

    @Builder(access = AccessLevel.PRIVATE)
    private Gym(String name, String tel, String address, BusinessTime businessTime) {
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.businessTime = businessTime;
    }

    public static Gym from(CreateGymRequest request) {
        BusinessTime businessTime = BusinessTime.create(request.getOpenAt(), request.getCloseAt(), request.getAllDay());
        return Gym.builder()
                .name(request.getName())
                .tel(request.getTel())
                .address(request.getAddress())
                .businessTime(businessTime)
                .build();
    }

    public void checkBusinessHour(LocalDateTime startAt, LocalDateTime endAt) {

        /*
         예약의 시작 시간이 종료 시간보다 선행되는지 체크
         exception이 발생 안하면 예약 시작 시간이 종료 시간보다 선행됨을 보장
         */
        if (startAt.isAfter(endAt))
            throw new CustomException(INVALID_RESERVATION_REQUEST);


        // 헬스장이 24시간 운영이면 true 리턴
        if (businessTime.isAllDay())
            return;

        // 헬스장 오픈 시각(ex. 6 : 30)
        LocalTime gymOpenAt = businessTime.getOpenAt();
        // 헬스장 종료 시각(ex. 23 : 45)
        LocalTime gymCloseAt = businessTime.getCloseAt();

        // 헬스장 오픈 및 종료 시각에서 '시' 정보만 추출
        int gymOpenHour = gymOpenAt.getHour();
        int gymCloseHour = gymCloseAt.getHour();

        /*
        openHour < closeHour 이면 (openHour < 예약의 시작 시간) && (예약 종료 시간 < closeHour) 인지만 확인
        openHour > closeHour 이면 예약의 시작과 종료 시간 둘 다 헬스장 운영 시간 내에 속하는지 확인
         */
        boolean result = checkStartAtInBusinessHour(startAt, gymOpenHour, gymCloseHour) &&
                checkEndAtInBusinessHour(endAt, gymOpenHour, gymCloseHour);

        if (!result)
            throw new CustomException(INVALID_RESERVATION_REQUEST, "요청이 헬스장 운영 시간과 맞지 않습니다.");
    }

    private boolean checkEndAtInBusinessHour(LocalDateTime endAt, int gymOpenHour, int gymCloseHour) {
        int reservationEndHour = endAt.getHour();
        int reservationEndMinute = endAt.getMinute();

        // 헬스장 종료 '시'와 예약의 종료 '시'가 같다면 '분' 비교
        if (reservationEndHour == gymCloseHour)
            return reservationEndMinute <= businessTime.getCloseAt().getMinute();
        // 헬스장 오픈 '시'와 예약의 종료 '시'가 같다면 '분' 비교
        if (reservationEndHour == gymOpenHour)
            return businessTime.getOpenAt().getMinute() < reservationEndMinute;

        /*
        openHour < closeHour (ex. 6AM ~ 23PM)이면 예약 종료 시간이 closeHour보다 앞서는지만 확인
        openHour > closeHour (ex. 4AM ~ 1AM)이면 예약 종료 시간이 헬스장 운영시간 안에 속하는지 확인
         */
        if (gymOpenHour < gymCloseHour)
            return reservationEndHour < gymCloseHour;
        else
            return (gymOpenHour < reservationEndHour) || (reservationEndHour < gymCloseHour);
    }

    private boolean checkStartAtInBusinessHour(LocalDateTime startAt, int gymOpenHour, int gymCloseHour) {
        int reservationStartHour = startAt.getHour();
        int reservationStartMinute = startAt.getMinute();

        // 헬스장 오픈 '시'와 예약의 시작 '시'가 같다면 '분' 비교
        if (reservationStartHour == gymOpenHour)
            return businessTime.getOpenAt().getMinute() <= reservationStartMinute;
        // 헬스장 종료 '시'와 예약의 시작 '시'가 같다면 '분' 비교
        if (reservationStartHour == gymCloseHour)
            return reservationStartMinute < businessTime.getCloseAt().getMinute();

        /*
        openHour < closeHour (ex. 6AM ~ 23PM)이면 예약 시작 시간이 openHour보다 더 늦은지만 확인
        openHour > closeHour (ex. 4AM ~ 1AM)이면 예약 시작 시간이 헬스장 운영시간 안에 속하는지 확인
         */
        if (gymOpenHour < gymCloseHour)
            return gymOpenHour < reservationStartHour;
        else
            return (gymOpenHour < reservationStartHour) || (reservationStartHour < gymCloseHour);
    }

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void updateTel(String tel) {
        if (tel != null) {
            this.tel = tel;
        }
    }

    public void updateAddress(String address) {
        if (address != null) {
            this.address = address;
        }
    }

    public void updateBusinessTime(LocalTime openAt, LocalTime closeAt, Boolean allDay) {
        if (openAt != null) {
            this.businessTime.updateOpenAt(openAt);
        }
        if (closeAt != null) {
            this.businessTime.updateCloseAt(closeAt);
        }
        if (allDay != null) {
            this.businessTime.updateAllDay(allDay);
        }
    }
}
