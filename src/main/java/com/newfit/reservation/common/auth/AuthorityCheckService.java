package com.newfit.reservation.common.auth;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.UserRepository;
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

    public void validateByUserId(Authentication authentication, Long userId) {
        User principal = (User) authentication.getPrincipal();
        com.newfit.reservation.domain.User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String nickname = principal.getUsername();
        if (!user.getNickname().equals(nickname)) {
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
}
