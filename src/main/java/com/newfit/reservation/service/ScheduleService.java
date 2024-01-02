package com.newfit.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newfit.reservation.common.fcm.dto.NotificationRequest;
import com.newfit.reservation.common.fcm.service.FCMService;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ReservationRepository reservationRepository;
	private final FCMService fcmService;
	private static final String NOTIFICATION_TITLE = "예약 5분 전 알림";

	@Scheduled(cron = "0 0/1 * * * *")
	private void notifyReservation() {
		LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
		LocalDateTime reservationTime = now.plusMinutes(5);
		List<Reservation> allByStartAt = reservationRepository.findAllByStartAt(reservationTime);
		List<NotificationRequest> notificationRequests = allByStartAt.stream()
			.map(reservation -> new NotificationRequest(reservation, NOTIFICATION_TITLE)).toList();
		for (NotificationRequest notificationRequest : notificationRequests) {
			fcmService.sendMessageByToken(notificationRequest);
		}
	}
}
