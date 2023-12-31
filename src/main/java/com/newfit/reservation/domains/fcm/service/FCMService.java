package com.newfit.reservation.domains.fcm.service;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.fcm.domain.FCMToken;
import com.newfit.reservation.domains.fcm.dto.request.FCMRegistrationRequest;
import com.newfit.reservation.domains.fcm.dto.request.NotificationRequest;
import com.newfit.reservation.domains.fcm.repository.FCMRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
	private final FirebaseMessaging firebaseMessaging;
	private final UserRepository userRepository;
	private final FCMRepository fcmRepository;

	public void registerFCMToken(Long userId, FCMRegistrationRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
		FCMToken fcmToken = FCMToken.createFCMToken(user, request.getFcmToken());

		fcmRepository.save(fcmToken);
	}

	public void sendMessageByToken(NotificationRequest request) {
		User user = userRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);

		String fcmToken = user.getFcmToken().getFcmToken();
		if (!fcmToken.isEmpty()) {
			Message message = getMessage(request, fcmToken);

			try {
				firebaseMessaging.send(message);
				log.info("푸시 알림 전송 완료 userId = {}", user.getId());
			} catch (FirebaseMessagingException e) {
				log.info("푸시 알림 전송 실패 userId = {}", user.getId());
				throw new RuntimeException(e);
			}
		}
	}

	private static Message getMessage(NotificationRequest request, String fcmToken) {
		Notification notification = Notification.builder()
			.setTitle(request.getTitle())
			.setBody(request.getBody())
			.build();

		Message message = Message.builder()
			.setToken(fcmToken)
			.setNotification(notification)
			.build();
		return message;
	}
}
