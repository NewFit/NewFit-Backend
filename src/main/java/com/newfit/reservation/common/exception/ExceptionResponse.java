package com.newfit.reservation.common.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExceptionResponse {

	private final LocalDateTime timestamp;
	private final int code;
	private final String error;
	private final String message;
	private final String notice;

	@Builder(access = AccessLevel.PRIVATE)
	private ExceptionResponse(CustomException exception) {
		this.timestamp = LocalDateTime.now();
		this.code = exception.getErrorCodeType().getStatusCode();
		this.error = exception.getErrorCodeType().name();
		this.message = exception.getErrorCodeType().getMessage();
		this.notice = exception.getNotice();
	}

	public static ExceptionResponse create(CustomException customException) {
		return ExceptionResponse.builder()
			.exception(customException)
			.build();
	}
}
