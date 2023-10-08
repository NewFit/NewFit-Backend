package com.newfit.reservation.common.jwt;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Role;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.auth.RefreshToken;
import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.auth.RefreshTokenRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.newfit.reservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TokenProvider {    // JWT의 생성 및 검증 로직 담당 클래스
    private final static Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private final static Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

    private final JwtProperties jwtProperties;
    private final AuthorityRepository authorityRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryAt = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());

        // 회원가입을 미실시한 사용자를 위한 JWT 생성
        if (user == null) {
            return generateDefaultToken(now, expiryAt);
        }

        List<Long> authorityIdList = user.getAuthorityList().stream().map(Authority::getId).collect(Collectors.toList());

        // 회원가입을 실시한 사용자를 위한 JWT 생성
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

    public boolean validAccessToken(String token, HttpServletRequest request) {
        Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
        checkAuthorityIdList(token, request);
        return true;
    }

    public boolean validRefreshToken(String token, HttpServletResponse response) {
        Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
        return true;
    }

    /*
     request가 변조되지 않았는지 체크하는 메소드
     JWT에 있는 authorityIdList속에 header에서 추출한 authorityId가 있는지 확인
     */
    private void checkAuthorityIdList(String token, HttpServletRequest request) {
        List<Integer> authorityIdList = getAuthorityIdList(token);
        Integer authorityId = authorityRepository.findById(Long.parseLong(request.getHeader("authority-id"))).orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND)).getId().intValue();
        authorityIdList
                .stream()
                .filter(authority -> authority.equals(authorityId))
                .findAny()
                .orElseThrow(() -> new CustomException(UNAUTHORIZED_REQUEST));
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        Claims claims = getClaims(token);
        Authority authority = authorityRepository.findById(Long.parseLong(request.getHeader("authority-id")))
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

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
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
