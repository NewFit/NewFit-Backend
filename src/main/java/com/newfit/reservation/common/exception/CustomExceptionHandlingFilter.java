package com.newfit.reservation.common.exception;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomExceptionHandlingFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomException exception) {
			ExceptionResponse exceptionResponse = ExceptionResponse.create(exception);
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(exception.getErrorCodeType().getStatusCode());
			objectMapper.writeValue(response.getWriter(), exceptionResponse);
		}
	}
}
