package com.newfit.reservation.domains.credit.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final AuthorityRepository authorityRepository;

	// 매일 자정에 모든 Authority의 크레딧 획득 횟수를 초기화
	@Scheduled(cron = "0 0 0 * * ?")
	private void resetCreditAcquisitionCount() {
		List<Authority> authorities = authorityRepository.findAllByCreditAcquisitionCountNotZero();
		authorities.forEach(Authority::resetAcquisitionCount);
	}
}
