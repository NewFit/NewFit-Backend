package com.newfit.reservation.common.auth.oauth.handler;

import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.common.auth.oauth.CustomOAuth2User;
import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.authority.domain.Authority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        OAuthHistory oAuthHistory = oAuth2User.getOAuthHistory();
        if (oAuthHistory.isRegistered()) {  // 회원가입이 완료된 경우
            Authority authority = oAuthHistory.getUser().getAuthorityList().stream().findAny().orElse(null);
            String accessToken = tokenProvider.generateAccessToken(oAuthHistory.getUser());
            String refreshToken = tokenProvider.generateRefreshToken(oAuthHistory.getUser());

            response.setHeader("access-token", accessToken);
            response.setHeader("refresh-token", refreshToken);
            log.info("AfterSignup.accessToken = {}", accessToken);
            if (authority != null) {    // 등록된 gym이 있는 경우
                response.setHeader("authority-id", authority.getId().toString());
            }
            else {  // 등록된 gym이 없는 경우
                response.setHeader("user-id", oAuthHistory.getUser().getId().toString());
            }
        } else {    // 회원가입이 미실시된 경우
            String accessToken = tokenProvider.generateAccessToken(oAuthHistory.getUser());
            log.info("BeforeSignup.accessToken = {}", accessToken);
            response.setHeader("access-token", accessToken);
            response.setHeader("oauth-history-id", oAuthHistory.getId().toString());
        }
    }
}
