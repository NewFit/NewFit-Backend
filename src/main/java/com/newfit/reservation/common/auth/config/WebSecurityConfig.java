package com.newfit.reservation.common.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newfit.reservation.common.auth.jwt.TokenAuthenticationFilter;
import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.common.auth.oauth.OAuth2AuthorizationRequestCookieRepository;
import com.newfit.reservation.common.auth.oauth.OAuth2UserCustomService;
import com.newfit.reservation.common.auth.oauth.handler.OAuth2FailureHandler;
import com.newfit.reservation.common.auth.oauth.handler.OAuth2SuccessHandler;
import com.newfit.reservation.common.exception.CustomExceptionHandlingFilter;
import com.newfit.reservation.domains.auth.repository.RefreshTokenRepository;
import com.newfit.reservation.domains.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.stream.Stream;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final static String AUTHENTICATION = "Authorization";
    private final static String BEARER = "Bearer ";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final ObjectMapper objectMapper;

    // 누구나 접근할 수 있는 URI 패턴을 정의
    private static final String[] PERMIT_ALL_PATTERNS = new String[] {
            "/login/**",
            "/logout/**"
    };

    // Spring Security가 무시하도록 할 요청을 정의
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(Stream.of(PERMIT_ALL_PATTERNS).map(AntPathRequestMatcher::antMatcher).toArray(AntPathRequestMatcher[]::new)).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/manager/**")).hasRole("MANAGER")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).authenticated()
                    .anyRequest().permitAll())
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customExceptionHandlingFilter(), TokenAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler)
                    .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestRepository(oAuth2AuthorizationRequestCookieRepository())))
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        tokenProvider.disableRefreshToken(getToken(request));
                    })
                    .logoutSuccessUrl("/login"))
                .build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, refreshTokenRepository, userRepository);
    }

    @Bean
    public OAuth2AuthorizationRequestCookieRepository oAuth2AuthorizationRequestCookieRepository() {
        return new OAuth2AuthorizationRequestCookieRepository();
    }

    @Bean
    public CustomExceptionHandlingFilter customExceptionHandlingFilter() {
        return new CustomExceptionHandlingFilter(objectMapper);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHENTICATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            return authorizationHeader.substring(BEARER.length());
        }
        return null;
    }
}