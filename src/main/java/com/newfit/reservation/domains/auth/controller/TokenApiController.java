package com.newfit.reservation.domains.auth.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.newfit.reservation.domains.auth.dto.request.IssueTokenRequest;
import com.newfit.reservation.domains.auth.dto.response.IssuedTokenResponse;
import com.newfit.reservation.domains.auth.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TokenApiController {
	private final TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<IssuedTokenResponse> login(@Valid @RequestBody IssueTokenRequest request) {
		IssuedTokenResponse response = tokenService.issueToken(request);
		return ResponseEntity
			.status(CREATED)
			.body(response);
	}
}