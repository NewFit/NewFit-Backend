package com.newfit.reservation.domains.user.service;


import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.auth.repository.OAuthHistoryRepository;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.credit.domain.Credit;
import com.newfit.reservation.domains.credit.repository.CreditRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.dto.request.UserSignUpRequest;
import com.newfit.reservation.domains.user.dto.request.UserUpdateRequest;
import com.newfit.reservation.domains.user.dto.response.AuthorityGymResponse;
import com.newfit.reservation.domains.user.dto.response.UserAuthorityInfoResponse;
import com.newfit.reservation.domains.user.dto.response.UserInfoResponse;
import com.newfit.reservation.domains.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.newfit.reservation.common.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final CreditRepository creditRepository;
    private final OAuthHistoryRepository oAuthHistoryRepository;
    private final TokenProvider tokenProvider;

    public void modify(Long userId, UserUpdateRequest request, HttpServletResponse response) {
        User updateUser = findOneById(userId);

        updateUser.updateEmail(request.getEmail());
        updateUser.updateTel(request.getTel());
        updateUser.updateFilePath(request.getUserProfileImage());
        if (request.getNickname() != null) {
            String nickname = request.getNickname();
            validateDuplicateNickname(nickname);
            updateUser.updateNickname(nickname);

            String accessToken = tokenProvider.generateAccessToken(updateUser);
            log.info("UserModify.accessToken = {}", accessToken);
            response.setHeader("access-token", accessToken);
        }
    }

    public UserInfoResponse userDetail(Long userId, Long authorityId) {
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
            return UserInfoResponse.createResponse(user, 0L);
        }

        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));
        User user = authority.getUser();

        AuthorityGymResponse current = new AuthorityGymResponse(authority);
        List<Authority> authorities = user.getAuthorityList();

        // current Authority를 제외한 나머지 Authority들만 추출
        List<AuthorityGymResponse> authorityGyms = authorities.stream()
                .filter(authorityIter -> !(authorityIter.getId().equals(authorityId)))
                .map(AuthorityGymResponse::new).toList();

        Long monthCredit = getMonthCredit(authority, now());

        return UserAuthorityInfoResponse.createResponse(user, monthCredit, current, authorityGyms);
    }

    private Long getMonthCredit(Authority authority, LocalDateTime now) {
        return creditRepository.findAllByAuthorityAndYearAndMonth(authority,
                        (short) now.getYear(),
                        (short) now.getMonthValue())
                .stream()
                .map(Credit::getAmount)
                .findAny()
                .orElse(0L);
    }

    public void drop(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findOneById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public void signUp(Long oauthHistoryId, UserSignUpRequest request, HttpServletResponse response) {
        OAuthHistory oAuthHistory = oAuthHistoryRepository
                .findById(oauthHistoryId)
                .orElseThrow(() -> new CustomException(OAUTH_HISTORY_NOT_FOUND));

        validateDuplicateNickname(request.getNickname());
        User user = User.userSignUp(request);
        userRepository.save(user);
        oAuthHistory.signUp(user);

        String accessToken = tokenProvider.generateAccessToken(user);
        log.info("UserSignup.accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }
}
