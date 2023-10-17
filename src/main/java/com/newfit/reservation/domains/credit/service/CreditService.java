package com.newfit.reservation.domains.credit.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.credit.dto.response.CreditRanking;
import com.newfit.reservation.domains.credit.dto.response.UserRankInfo;
import com.newfit.reservation.domains.credit.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.domains.credit.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.newfit.reservation.common.exception.ErrorCode.AUTHORITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditService {

    private final CreditRepository creditRepository;
    private final AuthorityRepository authorityRepository;

    public UserRankInfoListResponse getRankInGym(Long authorityId) {
        LocalDateTime now = LocalDateTime.now();
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        // 헬스장의 상위 랭킹 조회
        List<CreditRanking> creditList = creditRepository
                .findTop10ByGymIdAndYearAndMonth(authority.getGym().getId(), (short) now.getYear(), (short) now.getMonthValue());

        List<UserRankInfo> rankingList = creditList.stream()
                .map(UserRankInfo::new).toList();

        // 헬스장에서 사용자의 랭킹 조회
        UserRankInfo userRankInfo = creditRepository
                .findRank(authority.getId(),
                        authority.getGym().getId(),
                        (short) now.getYear(),
                        (short) now.getMonthValue())
                .map(UserRankInfo::new)
                .orElseGet(() -> UserRankInfo.noCredit(authority));

        return new UserRankInfoListResponse(authority.getGym().getName(), rankingList, userRankInfo);
    }
}