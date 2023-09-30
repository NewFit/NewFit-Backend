package com.newfit.reservation.common.config;

import com.newfit.reservation.common.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    // JWT가 담겨져 올 request header의 이름
    private final static String AUTHENTICATION = "Authorization";

    // 수신한 JWT가 Bearer 타입인지 체크하기 위한 필드
    private final static String BEARER = "Bearer ";
    private final TokenProvider tokenProvider;

    // Request Header에서 JWT 추출하고 현재 request의 URI에 따라 필요한 작업 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        if(request.getRequestURI().equals("/login") || accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if(requiresValidityCheck(request) && tokenProvider.validToken(accessToken, request)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            Authentication authentication = tokenProvider.getAnonymousAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // Request Header에서 JWT 추출
    private String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHENTICATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            return authorizationHeader.substring(BEARER.length());
        }
        return null;
    }

    /*
    Authority가 생성되지 않은 상태로 요청을 보낼 수 있는 URI에 대해서
    JWT validation을 진행하지 않도록 체크하는 메소드
     */
    private boolean requiresValidityCheck(HttpServletRequest request) {
        if (request.getRequestURI().equals("/api/v1/authority") && request.getMethod().equals(HttpMethod.POST.toString())) {
            return false;
        }
        if (request.getRequestURI().equals("/api/v1/gyms") && request.getMethod().equals(HttpMethod.GET.toString())) {
            return false;
        }
        return true;
    }
}
