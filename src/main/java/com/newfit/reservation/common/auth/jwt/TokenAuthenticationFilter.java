package com.newfit.reservation.common.auth.jwt;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.auth.domain.RefreshToken;
import com.newfit.reservation.domains.auth.repository.RefreshTokenRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

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
        if (request.getRequestURI().equals("/login") || token == null) {
            filterChain.doFilter(request, response);
        } else if (request.getRequestURI().equals("/refresh")) {
            tokenProvider.validToken(token);
            String accessToken = reIssueAccessToken(token);
            response.setHeader("access-token", accessToken);
        } else if (request.getHeader("authority-id") != null) {
            tokenProvider.validAccessToken(token, request, response);
            Authentication authentication = tokenProvider.getAuthentication(token, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            tokenProvider.validToken(token);
            Authentication authentication = tokenProvider.getAnonymousAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }

    private String reIssueAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new CustomException(REFRESH_TOKEN_NOT_FOUND));
        User user = userRepository.findById(refreshToken.getId()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
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
}
