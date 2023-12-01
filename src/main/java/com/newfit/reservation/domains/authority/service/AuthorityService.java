package com.newfit.reservation.domains.authority.service;

import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import com.newfit.reservation.domains.authority.dto.request.EntryRequest;
import com.newfit.reservation.domains.authority.dto.response.*;
import com.newfit.reservation.domains.authority.dto.response.manager.UserAndPendingListResponse;
import com.newfit.reservation.domains.authority.dto.response.manager.UserAndPendingResponse;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.repository.GymRepository;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.repository.ReservationRepository;
import com.newfit.reservation.domains.routine.domain.Routine;
import com.newfit.reservation.domains.routine.repository.EquipmentRoutineRepository;
import com.newfit.reservation.domains.routine.repository.RoutineRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final ReservationRepository reservationRepository;
    private final TokenProvider tokenProvider;
    private final EquipmentGymRepository equipmentGymRepository;
    private final RoutineRepository routineRepository;
    private final EquipmentRoutineRepository equipmentRoutineRepository;

    public void register(Long userId, Long gymId, HttpServletResponse response) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(GYM_NOT_FOUND));

        validateAuthorityDuplicate(userId, gymId);

        Authority authority = authorityRepository.save(Authority.createAuthority(user, gym));

        response.setHeader("authority-id", String.valueOf(authority.getId()));
    }

    private void validateAuthorityDuplicate(Long userId, Long gymId) {
        Authority authority = authorityRepository.findOneByUserIdAndGymIdAndRoleType(userId, gymId, RoleType.USER);
        if (authority != null) {
            throw new CustomException(DUPLICATE_AUTHORITY_REQUEST);
        }
    }

    public void delete(Long authorityId, HttpServletResponse response) {
        Authority authority = findById(authorityId);
        User user = authority.getUser();
        user.getAuthorityList().remove(authority);

        deleteRelatedRoutines(authority);
        deleteRelationWithReservation(authorityId);

        authorityRepository.delete(authority);

        String accessToken = tokenProvider.generateAccessToken(user);
        log.info("AuthorityDelete.accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);
    }

    private void deleteRelatedRoutines(Authority authority) {
        List<Routine> routines = routineRepository.findAllByAuthority(authority);
        routineRepository.deleteAll(routines);
    }

    private void deleteRelationWithReservation(Long authorityId) {
        List<Reservation> reservations = reservationRepository.findAllByAuthorityId(authorityId);
        reservations.forEach(Reservation::removeAuthority);
        reservationRepository.saveAllAndFlush(reservations);
    }

    public GymListResponse listRegistration(Long authorityId) {

        Long userId = findById(authorityId).getUser().getId();
        List<GymResponse> gyms = authorityRepository.findAllAuthorityByUserId(userId).stream()
                .map(GymResponse::new).toList();
        return GymListResponse.createResponse(gyms);
    }

    public Gym getGymByAuthorityId(Long authorityId) {
        return findById(authorityId).getGym();
    }

    /*
    특정 userId와 gymId를 가지면서 accepted 컬럼이 false인 단일 Authority를 조회하여
    accepted 컬럼값을 true로 업데이트 합니다. 그 다음에 업데이트 결과를 반환할 Dto를 생성후 반환합니다.
     */
    public void acceptUser(Long userId, Long gymId) {
        Authority authority = authorityRepository.findOneByUserIdAndGymIdAndRoleType(userId, gymId, RoleType.USER);
        if (authority == null)
            throw new CustomException(AUTHORITY_NOT_FOUND);
        if (authority.getAccepted())
            throw new CustomException(ALREADY_ACCEPTED_AUTHORITY);

        authority.acceptUser();
    }

    /*
    AuthorityRepository로부터 특정 Gym에 소속되어 있는 모든 Authority 객체를 리스트로 받습니다.
    그 다음에 각 Authority 객체의 accepted 필드값에 따라서 users와 requests로 나누어 리스트를 분리합니다.
    마지막으로 Dto를 생성하여 반환합니다.
     */
    public UserAndPendingListResponse getUserAndAcceptRequestList(Long authorityId) {
        Gym gym = getGymByAuthorityId(authorityId);

        List<Authority> authorities = authorityRepository.findAllAuthorityByGym(gym);
        Map<Boolean, List<UserAndPendingResponse>> classifyByAccepted = authorities.stream()
                .collect(Collectors.groupingBy(Authority::getAccepted,
                        Collectors.mapping(UserAndPendingResponse::new, Collectors.toList())));

        return UserAndPendingListResponse.createResponse(gym.getName(), classifyByAccepted);
    }

    public Authority findById(Long authorityId) {
        return authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));
    }

    public ReservationListResponse listAuthorityReservation(Long authorityId) {

        String gymName = findById(authorityId).getGym().getName();

        List<ReservationDetailResponse> reservationResponseList = reservationRepository
                .findAllByAuthorityId(authorityId)
                .stream()
                .map(ReservationDetailResponse::new).toList();


        return ReservationListResponse.createResponse(gymName, reservationResponseList);
    }

    public void enterGym(Long authorityId, EntryRequest request) {
        Authority authority = findById(authorityId);
        authority.updateTagAt(request.getTagAt());
    }

    public EquipmentGymListResponse findAllInGym(Gym gym) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);

        List<EquipmentResponse> equipmentResponses = allByGym.stream()
                .map(EquipmentResponse::new).toList();

        return new EquipmentGymListResponse(gym.getName(), allByGym.size(), equipmentResponses);
    }
}
