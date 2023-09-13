package com.newfit.reservation.service;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    /*
    userId로 조회
    repository가 반환한 Authority의 Gym 반환
     */
    public Gym getGym(Long userId) {
        return authorityRepository.findOneByUserId(userId).get().getGym();
    }
}
