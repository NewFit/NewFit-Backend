package com.newfit.reservation.common;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.fcm.dto.request.NotificationRequest;
import com.newfit.reservation.domains.fcm.service.FCMService;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ReservationRepository reservationRepository;
	private final FCMService fcmService;
	private final AuthorityRepository authorityRepository;

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

	// 매일 자정에 모든 Authority의 크레딧 획득 횟수를 초기화
	@Scheduled(cron = "0 0 0 * * ?")
	private void resetCreditAcquisitionCount() {
		List<Authority> authorities = authorityRepository.findAllByCreditAcquisitionCountNotZero();
		authorities.forEach(Authority::resetAcquisitionCount);
	}
}
