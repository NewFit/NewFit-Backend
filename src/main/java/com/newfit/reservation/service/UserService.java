package com.newfit.reservation.service;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserDetailResponse;
import com.newfit.reservation.dto.response.UserSimpleResponse;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.CreditRepository;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final CreditRepository creditRepository;

    public UserSimpleResponse modify(Long userId, UserUpdateRequest request) {
        User updateUser = findOneById(userId);

        if (request.getEmail() != null)
            updateUser.updateEmail(request.getEmail());

        if (request.getNickname() != null)
            updateUser.updateNickname(request.getNickname());

        if (request.getTel() != null)
            updateUser.updateTel(request.getTel());

        if (request.getUserProfileImage() != null)
            updateUser.updateFilePath(request.getUserProfileImage());


        return UserSimpleResponse.builder()
                .userId(userId)
                .build();
    }

    public UserDetailResponse userDetail(Long authorityId) {

        Authority authority = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);

        User user = authority.getUser();

        LocalDateTime now = LocalDateTime.now();

        Long monthCredit = creditRepository.findAllByAuthorityAndYearAndMonth(authority,
                        (short) now.getYear(),
                        (short) now.getMonthValue())
                .stream()
                .map(Credit::getAmount)
                .findAny()
                .orElse(0L);


        return UserDetailResponse.builder()
                .filePath(user.getFilePath())
                .nickname(user.getNickname())
                .totalCredit(user.getBalance())
                .monthCredit(monthCredit)
                .build();
    }

    public UserSimpleResponse drop(Long userId) {
        userRepository.deleteById(userId);
        return UserSimpleResponse.builder()
                .userId(userId)
                .build();
    }

    public User findOneById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
