package com.newfit.reservation.domains.authority.service;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import com.newfit.reservation.domains.authority.dto.request.admin.AssignManagerRequest;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminManagerService {
    private final AuthorityRepository authorityRepository;

    public void assignManager(AssignManagerRequest request) {
        Authority authority = authorityRepository.findOneByUserNicknameAndGymId(request.getNickname(), request.getGymId());

        authority.updateRoleType(RoleType.MANAGER);
        authority.acceptUser();
    }
}
