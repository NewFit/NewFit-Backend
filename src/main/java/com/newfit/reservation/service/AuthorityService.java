package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.Role;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.response.*;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.GymRepository;
import com.newfit.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /*
    특정 userId와 gymId를 가지면서 accepted 컬럼이 false인 단일 Authority를 조회하여
    accepted 컬럼값을 true로 업데이트 합니다. 그 다음에 업데이트 결과를 반환할 Dto를 생성후 반환합니다.
     */
    public UserAcceptResponse acceptUser(Long userId, Long gymId) {
        Authority authority = authorityRepository.findOneByUserIdAndGymIdAndAccepted(userId, gymId)
                .orElseThrow(IllegalArgumentException::new);
        authority.updateAccepted(true);

        return UserAcceptResponse.builder()
                .username(authority.getUser().getUsername())
                .build();
    }

    /*
    AuthorityRepository로부터 특정 Gym에 소속되어 있는 모든 Authority 객체를 리스트로 받습니다.
    그 다음에 각 Authority 객체의 accepted 필드값에 따라서 users와 requests로 나누어 리스트를 분리합니다.
    마지막으로 Dto를 생성하여 반환합니다.
     */
    public UserAndPendingListResponse getUserAndAcceptRequestList(Long gymId) {
        Gym findGym = gymRepository.findById(gymId).orElseThrow(IllegalArgumentException::new);
        String gymName = findGym.getName();

        List<Authority> authorities = authorityRepository.findAuthoritiesByGym(gymId);

        List<UserAndPendingResponse> requests = new ArrayList<>();
        List<UserAndPendingResponse> users = new ArrayList<>();

        for (Authority authority : authorities) {
            UserAndPendingResponse response = new UserAndPendingResponse(authority);

            if(authority.getAccepted())
                users.add(response);
            else
                requests.add(response);
        }

        return UserAndPendingListResponse.builder()
                .gymName(gymName)
                .requests(requests)
                .users(users)
                .build();
    }
}
