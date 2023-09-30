package com.newfit.reservation.service;

import com.newfit.reservation.common.fcm.dto.NotificationRequest;
import com.newfit.reservation.common.fcm.service.FCMService;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ReservationRepository reservationRepository;
    private final FCMService fcmService;
    private static final String NOTIFICATION_TITLE = "예약 5분 전 알림";

    @Scheduled(cron ="0 0/1 * * * *")
    private void notifyReservation() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime reservationTime = now.plusMinutes(5);
        List<Reservation> allByStart_at = reservationRepository.findAllByStart_at(reservationTime);
        List<NotificationRequest> notificationRequests = allByStart_at.stream()
                .map(reservation -> new NotificationRequest(reservation, NOTIFICATION_TITLE)).toList();
        for (NotificationRequest notificationRequest : notificationRequests) {
            fcmService.sendMessageByToken(notificationRequest);
        }
    }
}
