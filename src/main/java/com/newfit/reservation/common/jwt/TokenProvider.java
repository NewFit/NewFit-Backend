package com.newfit.reservation.common.jwt;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.repository.AuthorityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
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

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final AuthorityRepository authorityRepository;

    public String generateToken(User user, Duration duration) {
        Date now = new Date();
        Date expiryAt = new Date(now.getTime() + duration.toMillis());
        if (user == null) {
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuer(jwtProperties.getIssuer())
                    .setIssuedAt(now)
                    .setExpiration(expiryAt)
                    .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                    .compact();
        }

        List<Long> authorityIdList = user.getAuthorityList().stream().map(Authority::getId).collect(Collectors.toList());

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

    public boolean validToken(String token, HttpServletRequest request) {
       try {
           Jwts.parser()
                   .setSigningKey(jwtProperties.getSecretKey())
                   .parseClaimsJws(token);
           checkAuthorityIdList(token, request);
           return true;
       } catch (Exception exception) {
           return false;
       }
    }

    private void checkAuthorityIdList(String token, HttpServletRequest request) {
        List<Long> authorityIdList = getAuthorityIdList(token);
        Long authorityId = authorityRepository.findOne(Long.parseLong(request.getHeader("authorityId"))).orElseThrow(IllegalArgumentException::new).getId();
        authorityIdList
                .stream()
                .filter(authority -> authority.equals(authorityId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        Claims claims = getClaims(token);
        Authority authority = authorityRepository.findOne(Long.parseLong(request.getHeader("authorityId")))
                .orElseThrow(IllegalArgumentException::new);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(authority.getRole().getDescription()));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 등록된 Gym이 없는 회원의 Authentication을 반환
    public Authentication getAnonymousAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(Role.GUEST.getDescription()));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    public List<Integer> getAuthorityIdList(String token) {
        Claims claims = getClaims(token);
        return claims.get("authorityIdList", List.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
