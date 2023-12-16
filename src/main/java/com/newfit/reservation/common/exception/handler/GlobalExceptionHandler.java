package com.newfit.reservation.common.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.common.exception.ErrorCodeType;
import com.newfit.reservation.common.exception.ExceptionResponse;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ExceptionResponse> customException(CustomException exception) {
		return ResponseEntity
			.status(exception.getErrorCodeType().getStatusCode())
			.body(ExceptionResponse.create(exception));
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<RuntimeException> internalServerException(RuntimeException exception) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(exception);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ExceptionResponse> enumValidationException() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ExceptionResponse.create(new CustomException(ErrorCodeType.INVALID_ENUM_VALUE)));
	}
}
