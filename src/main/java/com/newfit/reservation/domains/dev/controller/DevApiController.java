package com.newfit.reservation.domains.dev.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domains.dev.dto.request.CreateProposalRequest;
import com.newfit.reservation.domains.dev.dto.request.CreateReportRequest;
import com.newfit.reservation.domains.dev.service.ProposalService;
import com.newfit.reservation.domains.dev.service.ReportService;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
public class DevApiController {

	private final ReportService reportService;
	private final ProposalService proposalService;
	private final UserService userService;
	private final AuthorityCheckService authorityCheckService;

	@PostMapping("/bug")
	public ResponseEntity<Void> createBugReport(Authentication authentication,
		@RequestHeader(value = "user-id") Long userId,
		@Valid @RequestBody CreateReportRequest request) {
		authorityCheckService.validateByUserId(authentication, userId);

		User user = userService.findOneById(userId);
		reportService.saveReport(user, request);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}

	@PostMapping("/feature")
	public ResponseEntity<Void> createFeatureProposal(Authentication authentication,
		@RequestHeader(value = "user-id") Long userId,
		@Valid @RequestBody CreateProposalRequest request) {
		authorityCheckService.validateByUserId(authentication, userId);

		User user = userService.findOneById(userId);
		proposalService.saveProposal(user, request);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.build();
	}
}
