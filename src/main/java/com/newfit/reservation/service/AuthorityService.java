package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.response.GymResponseDto;
import com.newfit.reservation.dto.response.ListResponseDto;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.GymRepository;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    public Long register(Long userId, Long gymId) {

        User user = userRepository.findOne(userId)
                .orElseThrow(IllegalArgumentException::new);
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(IllegalArgumentException::new);

        Authority authority = Authority.builder()
                .user(user)
                .gym(gym)
                .build();

        return authorityRepository.save(authority);
    }

    public void delete(Long userId, Long gymId) {
        authorityRepository.deleteByUserIdAndGymId(userId, gymId);
    }

    public ListResponseDto<GymResponseDto> list(Long id) {
        return ListResponseDto.<GymResponseDto>builder()
                .data(authorityRepository.findAuthoritiesByUserId(id)
                        .stream()
                        .map(GymResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
