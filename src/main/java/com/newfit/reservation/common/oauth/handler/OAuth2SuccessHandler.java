package com.newfit.reservation.common.oauth.handler;

import com.newfit.reservation.common.jwt.TokenProvider;
import com.newfit.reservation.common.oauth.CustomOAuth2User;
import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.auth.OAuthHistory;
import com.newfit.reservation.domain.auth.RefreshToken;
import com.newfit.reservation.repository.auth.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final static Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private final static Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        OAuthHistory oAuthHistory = oAuth2User.getOAuthHistory();
        if (oAuthHistory.isRegistered()) {
            Authority authority = oAuthHistory.getUser().getAuthorityList().stream().findAny().orElse(null);
            String accessToken = tokenProvider.generateToken(oAuthHistory.getUser(), ACCESS_TOKEN_DURATION);
            String refreshToken = tokenProvider.generateToken(oAuthHistory.getUser(), REFRESH_TOKEN_DURATION);
            refreshTokenRepository.save(RefreshToken.createRefreshToken(oAuthHistory.getUser(), refreshToken));

            response.setHeader("accessToken", accessToken);
            response.setHeader("refreshToken", refreshToken);
            if (authority != null) {
                response.setHeader("authorityId", authority.getId().toString());
                String redirectUri = getTargetUri(authority.getGym().getId());
                response.sendRedirect(redirectUri);
            }
            else {
                response.sendRedirect("/api/v1/gyms");
            }
        } else {
            String accessToken = tokenProvider.generateToken(oAuthHistory.getUser(), ACCESS_TOKEN_DURATION);
            response.setHeader("accessToken", accessToken);
            // TODO: 회원 가입 추가 정보 입력 페이지로 이동
            response.sendRedirect("/test-jwt");
        }
    }

    public String getTargetUri(Long gymId) {
        return UriComponentsBuilder.fromUriString("/api/v1/equipments")
                .queryParam("gym_id", gymId)
                .build().toUriString();
    }
}
