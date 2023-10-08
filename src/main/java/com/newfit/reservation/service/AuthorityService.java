package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.Role;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.response.*;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.GymRepository;
import com.newfit.reservation.repository.UserRepository;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final ReservationRepository reservationRepository;

    public void register(Long userId, Long gymId) {

        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(IllegalArgumentException::new);

        Authority authority = Authority.createAuthority(user, gym);

        authorityRepository.save(authority);
    }

    public void delete(Long authorityId) {
        authorityRepository.deleteById(authorityId);
    }

    public GymListResponse listRegistration(Long authorityId) {

        Long userId = findById(authorityId).getUser().getId();
        List<GymResponse> gyms = authorityRepository.findAllAuthorityByUserId(userId).stream()
                .map(GymResponse::new)
                .toList();
        return GymListResponse.createResponse(gyms);
    }

    public Gym getGymByAuthorityId(Long authorityId) {
        return authorityRepository.findById(authorityId).get().getGym();
    }

    /*
    특정 userId와 gymId를 가지면서 accepted 컬럼이 false인 단일 Authority를 조회하여
    accepted 컬럼값을 true로 업데이트 합니다. 그 다음에 업데이트 결과를 반환할 Dto를 생성후 반환합니다.
     */
    public void acceptUser(Long userId, Long gymId) {
        Authority authority = authorityRepository.findOneByUserIdAndGymIdAndRole(userId, gymId, Role.USER);
        if (authority == null || authority.getAccepted())
            throw new IllegalArgumentException();

        authority.acceptUser();
    }

    /*
    AuthorityRepository로부터 특정 Gym에 소속되어 있는 모든 Authority 객체를 리스트로 받습니다.
    그 다음에 각 Authority 객체의 accepted 필드값에 따라서 users와 requests로 나누어 리스트를 분리합니다.
    마지막으로 Dto를 생성하여 반환합니다.
     */
    public UserAndPendingListResponse getUserAndAcceptRequestList(Long authorityId) {
        Gym gym = getGymByAuthorityId(authorityId);
        String gymName = gym.getName();

        List<Authority> authorities = authorityRepository.findAllAuthorityByGym(gym);

        List<UserAndPendingResponse> requests = new ArrayList<>();
        List<UserAndPendingResponse> users = new ArrayList<>();

        for (Authority authority : authorities) {
            UserAndPendingResponse response = new UserAndPendingResponse(authority);

            if (authority.getAccepted())
                users.add(response);
            else
                requests.add(response);
        }

        return UserAndPendingListResponse.createResponse(gymName, requests, users);
    }

    public Authority findById(Long authorityId) {
        return authorityRepository.findById(authorityId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public ReservationListResponse listAuthorityReservation(Long authorityId) {

        String gymName = findById(authorityId).getGym().getName();

        List<ReservationDetailResponse> reservationResponseList = reservationRepository
                .findAllByAuthorityId(authorityId)
                .stream()
                .map(ReservationDetailResponse::new)
                .toList();


        return ReservationListResponse.createResponse(gymName, reservationResponseList);
    }
}
