package com.newfit.reservation.domains.fcm.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.domains.fcm.dto.request.FCMRegistrationRequest;
import com.newfit.reservation.domains.fcm.service.FCMService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMApiController {

	private final FCMService fcmService;

	@PostMapping
	public ResponseEntity<Void> fcmTokenRegistration(
		@RequestHeader(value = "user-id") Long userId,
		@Valid @RequestBody FCMRegistrationRequest request) {
		fcmService.registerFCMToken(userId, request);
		return ResponseEntity
			.status(CREATED)
			.build();
	}
}
