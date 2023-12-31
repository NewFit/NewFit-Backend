package com.newfit.reservation.common.auth.oauth;

import java.io.IOException;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

import com.newfit.reservation.common.auth.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2AuthorizationRequestCookieRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	private final static String OAUTH2_AUTHORIZATION_REQUEST = "oauth2_authorization_request";
	private final static int COOKIE_EXPIRY_SECONDS = 18000;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		try {
			Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST);
			return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
		} catch (IOException exception) {
			return null;
		}
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		if (authorizationRequest == null) {
			removeAuthorizationRequestCookies(request, response);
			return;
		}
		CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST, CookieUtil.serialize(authorizationRequest),
			COOKIE_EXPIRY_SECONDS);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
	}
}
