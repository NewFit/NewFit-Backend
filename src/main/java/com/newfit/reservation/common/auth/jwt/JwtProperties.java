package com.newfit.reservation.common.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class JwtProperties {    // JWT 관련 설정 정보를 담는 클래스
	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.secret_key}")
	private byte[] secretKey;
}
