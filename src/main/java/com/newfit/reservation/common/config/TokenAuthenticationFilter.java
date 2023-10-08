package com.newfit.reservation.common.config;

import com.newfit.reservation.common.jwt.TokenProvider;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.auth.RefreshToken;
import com.newfit.reservation.repository.UserRepository;
import com.newfit.reservation.repository.auth.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // Request Header에서 JWT 추출하고 현재 request의 URI에 따라 필요한 작업 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if(request.getRequestURI().equals("/login") || token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getRequestURI().equals("/refresh") && tokenProvider.validRefreshToken(token, response)) {
            String accessToken = reIssueAccessToken(token);
            response.setHeader("access-token", accessToken);
            filterChain.doFilter(request, response);
            return;
        }
        if(requiresValidityCheck(request) && tokenProvider.validAccessToken(token, request)) {
            Authentication authentication = tokenProvider.getAuthentication(token, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            Authentication authentication = tokenProvider.getAnonymousAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String reIssueAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findById(refreshToken.getId()).orElseThrow(IllegalArgumentException::new);
        return tokenProvider.generateAccessToken(user);
    }

    // Request Header에서 JWT 추출
    private String getToken(HttpServletRequest request) {
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
