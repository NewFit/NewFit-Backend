package com.newfit.reservation.service;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findOneById(Long userId) {
        return userRepository.findOne(userId);
    }
}
