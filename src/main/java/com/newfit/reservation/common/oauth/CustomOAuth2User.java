package com.newfit.reservation.common.oauth;

import com.newfit.reservation.domain.auth.OAuthHistory;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private OAuthHistory oAuthHistory;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, OAuthHistory oAuthHistory) {
        super(authorities, attributes, nameAttributeKey);
        this.oAuthHistory = oAuthHistory;
    }
}
