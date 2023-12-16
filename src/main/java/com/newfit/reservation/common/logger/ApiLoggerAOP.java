package com.newfit.reservation.common.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ApiLoggerAOP {

	@Around("execution(* com.newfit.reservation.domains..controller..*(..))")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		String headerName = getHeaderName(request);
		beforeApiCall(joinPoint, request, headerName);

		try {
			return joinPoint.proceed();
		} finally {
			afterApiCall(request, headerName);
		}
	}

	private void beforeApiCall(ProceedingJoinPoint joinPoint, HttpServletRequest request, String headerName) {
		if (headerName != null) {
			log.info("request: method = {}, {} = {}", joinPoint.getSignature().getName(),
				headerName, request.getHeader(headerName));
		}
	}

	private String getHeaderName(HttpServletRequest request) {
		if (request.getHeader("authority-id") != null) {
			return "authority-id";
		} else if (request.getHeader("user-id") != null) {
			return "user-id";
		} else if (request.getHeader("oauth-history-id") != null) {
			return "oauth-history-id";
		}

		return null;
	}

	private void afterApiCall(HttpServletRequest request, String headerName) {
		if (headerName != null) {
			log.info("response: {} = {}", headerName, request.getHeader(headerName));
		}
	}
}