package com.newfit.reservation.common.auth.jwt;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.auth.domain.RefreshToken;
import com.newfit.reservation.domains.auth.repository.RefreshTokenRepository;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.Role;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static com.newfit.reservation.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {    // JWT의 생성 및 검증 로직 담당 클래스
    private final static Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private final static Duration ACCESS_TEMPORARY_TOKEN_DURATION = Duration.ofMinutes(10);
    private final static Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

    private final JwtProperties jwtProperties;
    private final AuthorityRepository authorityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String generateAccessToken(User user) {
        Date now = new Date();

        if (user == null) {     // 회원가입을 미실시한 사용자
            Date expiryAt = new Date(now.getTime() + ACCESS_TEMPORARY_TOKEN_DURATION.toMillis());
            return generateDefaultToken(now, expiryAt);
        }

        List<Long> authorityIdList = user.getAuthorityList().stream().map(Authority::getId).toList();
        if (authorityIdList.isEmpty()) {    // 가입된 헬스장이 없는 사용자
            Date expiryAt = new Date(now.getTime() + ACCESS_TEMPORARY_TOKEN_DURATION.toMillis());
            return generateAccessToken(user, now, expiryAt, authorityIdList);
        } else {
            Date expiryAt = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());
            return generateAccessToken(user, now, expiryAt, authorityIdList);
        }
    }

    private String generateAccessToken(User user, Date now, Date expiryAt, List<Long> authorityIdList) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryAt)
                .setSubject(user.getNickname())
                .claim("authorityIdList", authorityIdList)
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    private String generateDefaultToken(Date now, Date expiryAt) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryAt)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiryAt = new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis());
        String token = generateDefaultToken(now, expiryAt);

        return refreshTokenRepository.save(RefreshToken.createRefreshToken(user, token))
                .getToken();
    }

    public void validAccessToken(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        validToken(token);
        try {
            checkAuthorityIdList(token, request);
        } catch (CustomException exception) {
            checkExceptionAndProceed(request, response, exception, token);
        }
    }

    private void checkExceptionAndProceed(HttpServletRequest request, HttpServletResponse response,
                                          CustomException exception, String token) throws IOException {
        if (!exception.getErrorCode().equals(OUTDATED_TOKEN)) {
            throw exception;
        }
        Long userId = getUserId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String accessToken = generateAccessToken(user);
        log.info("AcceptedUser.accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);
    }

    public void validToken(String token) {
        Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
    }

    /*
     request가 변조되지 않았는지 체크하는 메소드
     JWT에 있는 authorityIdList속에 header에서 추출한 authorityId가 있는지 확인
     */
    private void checkAuthorityIdList(String token, HttpServletRequest request) {
        List<Integer> authorityIdList = getAuthorityIdList(token);
        Long authorityId = extractAuthorityId(request);

        if (!authorityIdList.contains(authorityId.intValue())) {
            // token에 있는 userId로 User 조회
            User user = userRepository.findById(getUserId(token))
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            // findAny로 찾지 못함 == 아직 accept 되지 않음 or 헤더에 넣은 것이 자신의 authority-id가 아님
            user.getAuthorityList().stream()
                    .filter(authority -> authority.getId().equals(authorityId) && authority.getAccepted())
                    .findAny()
                    .orElseThrow(() -> new CustomException(UNAUTHORIZED_REQUEST));

            // JWT 새로 발급해야 하는 경우
            throw new CustomException(OUTDATED_TOKEN);
        }
    }

    private Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Long extractAuthorityId(HttpServletRequest request) {
        return Long.parseLong(request.getHeader("authority-id"));
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        Claims claims = getClaims(token);
        Authority authority = authorityRepository.findById(extractAuthorityId(request))
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(authority.getRole().getDescription()));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    /*
     Anonymous 회원을 위한 Authentication을 반환
     Anonymous : 아직 자신의 헬스장을 등록하지 않은 사용자
     */
    public Authentication getAnonymousAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(Role.GUEST.getDescription()));

        if (claims.getSubject() != null) {
            return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
        } else {
            return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User("anonymous", "", authorities), token, authorities);
        }
    }

    private List<Integer> getAuthorityIdList(String token) {
        Claims claims = getClaims(token);
        return claims.get("authorityIdList", List.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public void disableRefreshToken(String accessToken) {
        Claims claims = getClaims(accessToken);
        Long userId = claims.get("id", Long.class);
        refreshTokenRepository.deleteById(userId);
    }
}
