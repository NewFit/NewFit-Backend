package com.newfit.reservation.service;


import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserDetailResponse;
import com.newfit.reservation.dto.response.UserSimpleResponse;
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

    public UserSimpleResponse modify(Long userId, UserUpdateRequest request) {
        User updateUser = findOneById(userId);

        updateUser.updateEmail(request.getEmail());
        updateUser.updateNickname(request.getNickname());
        updateUser.updateTel(request.getTel());
        updateUser.updateFilePath(request.getUserProfileImage());


        return UserSimpleResponse.builder()
                .userId(userId)
                .build();
    }

    public UserDetailResponse userDetail(Long userId) {

        User user = findOneById(userId);

        Long monthCredit = user.getTermCredit(LocalDateTime.now());


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
