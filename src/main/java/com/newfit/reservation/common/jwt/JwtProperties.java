package com.newfit.reservation.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtProperties {    // JWT 관련 설정 정보를 담는 클래스
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret_key}")
    private byte[] secretKey;
}
