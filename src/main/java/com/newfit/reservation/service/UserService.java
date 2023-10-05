package com.newfit.reservation.service;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.auth.OAuthHistory;
import com.newfit.reservation.dto.request.UserSignUpRequest;
import com.newfit.reservation.dto.request.UserUpdateRequest;
import com.newfit.reservation.dto.response.AuthorityGymResponse;
import com.newfit.reservation.dto.response.UserDetailResponse;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.CreditRepository;
import com.newfit.reservation.repository.UserRepository;
import com.newfit.reservation.repository.auth.OAuthHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final CreditRepository creditRepository;
    private final OAuthHistoryRepository oAuthHistoryRepository;

    public void modify(Long userId, UserUpdateRequest request) {
        User updateUser = findOneById(userId);

        if (request.getEmail() != null)
            updateUser.updateEmail(request.getEmail());

        if (request.getNickname() != null)
            updateUser.updateNickname(request.getNickname());

        if (request.getTel() != null)
            updateUser.updateTel(request.getTel());

        if (request.getUserProfileImage() != null)
            updateUser.updateFilePath(request.getUserProfileImage());
    }

    public UserDetailResponse userDetail(Long authorityId) {

        Authority authority = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);

        User user = authority.getUser();
        List<Authority> authorities = user.getAuthorityList();

        AuthorityGymResponse current = new AuthorityGymResponse(authority);

        // current Authority를 제외한 나머지 Authority들만 추출
        List<AuthorityGymResponse> authorityGyms = authorities.stream()
                .filter(authorityIter -> !(authorityIter.getId().equals(authorityId)))
                .map(AuthorityGymResponse::new)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        Long monthCredit = creditRepository.findAllByAuthorityAndYearAndMonth(authority,
                        (short) now.getYear(),
                        (short) now.getMonthValue())
                .stream()
                .map(Credit::getAmount)
                .findAny()
                .orElse(0L);

        return UserDetailResponse.createUserDetailResponse(user, monthCredit, current, authorityGyms);
    }

    public void drop(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findOneById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void signUp(Long oauthHistoryId, UserSignUpRequest request) {
        OAuthHistory oAuthHistory = oAuthHistoryRepository.findById(oauthHistoryId).orElseThrow(IllegalArgumentException::new);
        User user = User.userSignUp(request, oAuthHistory.getProvider());
        userRepository.save(user);
        oAuthHistory.signUp(user);
    }
}
