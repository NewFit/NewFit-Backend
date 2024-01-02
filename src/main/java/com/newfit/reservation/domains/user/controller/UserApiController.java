package com.newfit.reservation.domains.user.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domains.user.dto.request.UserDropRequest;
import com.newfit.reservation.domains.user.dto.request.UserSignUpRequest;
import com.newfit.reservation.domains.user.dto.request.UserUpdateRequest;
import com.newfit.reservation.domains.user.dto.response.UserInfoResponse;
import com.newfit.reservation.domains.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

	private final UserService userService;
	private final AuthorityCheckService authorityCheckService;

	@PatchMapping
	public ResponseEntity<Void> modify(Authentication authentication,
		HttpServletResponse response,
		@RequestHeader(value = "user-id") Long userId,
		@Valid @RequestBody UserUpdateRequest request) {
		authorityCheckService.validateByUserId(authentication, userId);
		userService.modify(userId, request, response);
		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping
	public ResponseEntity<UserInfoResponse> userDetail(@RequestHeader(value = "user-id", required = false) Long userId,
		@RequestHeader(value = "authority-id", required = false) Long authorityId) {
		UserInfoResponse response = userService.userDetail(userId, authorityId);
		return ResponseEntity
			.ok(response);
	}

	@DeleteMapping
	public ResponseEntity<Void> drop(Authentication authentication,
		@RequestHeader(value = "user-id") Long userId,
		@Valid @RequestBody UserDropRequest request) {
		authorityCheckService.validateByUserId(authentication, userId);
		userService.drop(userId, request.getEmail());
		return ResponseEntity
			.noContent()
			.build();
	}

	@PostMapping
	public ResponseEntity<Void> signUp(@RequestHeader(value = "oauth-history-id") Long oauthHistoryId,
		@Valid @RequestBody UserSignUpRequest request,
		HttpServletResponse response) {
		userService.signUp(oauthHistoryId, request, response);

		return ResponseEntity
			.status(CREATED)
			.build();
	}
}
