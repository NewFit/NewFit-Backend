package com.newfit.reservation.common.auth.oauth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.common.auth.oauth.CustomOAuth2User;
import com.newfit.reservation.domains.auth.domain.OAuthHistory;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;

	@Value("${admin.attributeName}")
	private String attributeName;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		OAuthHistory oAuthHistory = oAuth2User.getOAuthHistory();

		if (oAuthHistory.getAttributeName().equals(attributeName)) {
			Cookie cookie = new Cookie("admin-token", tokenProvider.generateAdminToken());
			cookie.setPath("/v1/admin");
			response.addCookie(cookie);
			response.sendRedirect("/v1/admin");
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.sendRedirect("/login");
		}
	}
}
