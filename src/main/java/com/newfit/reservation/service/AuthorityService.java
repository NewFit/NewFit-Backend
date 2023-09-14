package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.Role;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.GymResponseDto;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.GymRepository;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public GymListResponse listRegistration(Long id) {
        return GymListResponse.builder()
                .gyms(authorityRepository.findAuthoritiesByUserId(id)
                        .stream()
                        .map(GymResponseDto::new)
                        .collect(Collectors.toList())
                )
                .build();
    }

    /*
    userId로 조회
    repository가 반환한 Authority의 Gym 반환
     */
    public Gym getGym(Long userId, Long gymId, Role role) {
        return authorityRepository.findOneByUserIdAndGymIdAndRole(userId, gymId, role).getGym();
    }
}
