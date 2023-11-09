package com.newfit.reservation.domains.authority.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import com.newfit.reservation.domains.authority.dto.request.admin.AssignManagerRequest;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.repository.GymRepository;
import com.newfit.reservation.domains.user.domain.User;
import com.newfit.reservation.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminManagerService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final GymRepository gymRepository;

    public void assignManager(AssignManagerRequest request) {
        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Gym gym = gymRepository.findById(request.getGymId())
                .orElseThrow(() -> new CustomException(GYM_NOT_FOUND));
        Authority authority = authorityRepository.findByUserAndGym(user, gym)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        authority.updateRoleType(RoleType.MANAGER);
        authority.updateAccepted(true);
    }
}
