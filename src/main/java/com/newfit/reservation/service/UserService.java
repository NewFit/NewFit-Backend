package com.newfit.reservation.service;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.routine.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserSimpleResponse;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        updateUser.updateFilePath(request.getImage());


        return UserSimpleResponse.builder()
                .userId(userId)
                .build();
    }


    public User findOneById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
