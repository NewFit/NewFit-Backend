package com.newfit.reservation.service;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User findOneById(Long userId) {
        return userRepository.findOne(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
