package com.newfit.reservation.common.auth;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static com.newfit.reservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthorityCheckService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public void checkHeaderAndValidateAuthority(Authentication authentication, HttpServletRequest request) {
        if (request.getHeader("user-id") != null) {
            validateByUserId(authentication, Long.parseLong(request.getHeader("user-id")));
        } else if (request.getHeader("authority-id") != null){
            validateByAuthorityId(authentication,Long.parseLong(request.getHeader("authority-id")));
        } else {
            throw new CustomException(UNAUTHORIZED_REQUEST);
        }
    }

    public void validateByAuthorityId(Authentication authentication, Long authorityId) {
        User principal = (User) authentication.getPrincipal();
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        String nickname = principal.getUsername();
        if (!authority.getUser().getNickname().equals(nickname)) {
            throw new CustomException(UNAUTHORIZED_REQUEST);
        }
    }

    private void validateByUserId(Authentication authentication, Long userId) {
        User principal = (User) authentication.getPrincipal();
        com.newfit.reservation.domain.User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String nickname = principal.getUsername();
        if (!user.getNickname().equals(nickname)) {
            throw new CustomException(UNAUTHORIZED_REQUEST);
        }
    }
}
