package com.newfit.reservation.common.auth;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return userRepository.findByNickname(nickname)
                .orElseThrow(IllegalArgumentException::new);
    }
}
