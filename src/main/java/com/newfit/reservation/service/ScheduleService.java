package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final AuthorityRepository authorityRepository;

    // 매일 자정에 모든 Authority의 크레딧 획득 횟수를 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    private void resetCreditAcquisitionCount() {
        List<Authority> authorities = authorityRepository.findAll();
        authorities.forEach(Authority::resetAcquisitionCount);
    }
}
