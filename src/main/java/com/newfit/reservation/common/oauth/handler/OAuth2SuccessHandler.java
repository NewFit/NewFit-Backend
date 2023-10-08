package com.newfit.reservation.common.oauth.handler;

import com.newfit.reservation.common.jwt.TokenProvider;
import com.newfit.reservation.common.oauth.CustomOAuth2User;
import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.auth.OAuthHistory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


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
            if (authority != null) {    // 등록된 gym이 있는 경우
                response.setHeader("authority-id", authority.getId().toString());
            }
            else {  // 등록된 gym이 없는 경우
                response.setHeader("user-id", oAuthHistory.getUser().getId().toString());
            }
        } else {    // 회원가입이 미실시된 경우
            String accessToken = tokenProvider.generateAccessToken(oAuthHistory.getUser());
            response.setHeader("access-token", accessToken);
            response.setHeader("oauth-history-id", oAuthHistory.getId().toString());
        }
    }
}
