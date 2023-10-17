package com.newfit.reservation.domains.credit.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.credit.domain.Credit;
import com.newfit.reservation.domains.credit.dto.response.CreditRanking;
import com.newfit.reservation.domains.credit.dto.response.UserRankInfo;
import com.newfit.reservation.domains.credit.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.domains.credit.repository.CreditRepository;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.newfit.reservation.common.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditService {

    private final CreditRepository creditRepository;
    private final AuthorityRepository authorityRepository;

    public UserRankInfoListResponse getRankInGym(Long authorityId) {
        LocalDateTime now = now();
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

    public void checkConditionAndAddCredit(Reservation reservation, Authority authority, LocalDateTime endEquipmentUseAt) {
        LocalDateTime now = now();

        if (checkConditions(reservation, endEquipmentUseAt)) {
            if (authority.getCreditAcquisitionCount() != 10) {
                Credit credit = creditRepository.findByAuthorityAndYearAndMonth(authority, (short) now.getYear(), (short) now.getMonthValue())
                        .orElseGet(() -> creditRepository.save(Credit.createCredit(authority)));
                credit.addAmount();
                authority.incrementAcquisitionCount();
                authority.getUser().addBalance(100L);
            } else {
                throw new CustomException(MAXIMUM_CREDIT_LIMIT);
            }
        } else {
            throw new CustomException(INVALID_CREDIT_ACQUIRE_REQUEST, "크레딧 획득에 실패했습니다.");
        }
    }

    private boolean checkConditions(Reservation reservation, LocalDateTime endTagAt) {
        return isStartTagInTime(reservation) && isEndTagInTime(reservation, endTagAt);
    }

    private boolean isStartTagInTime(Reservation reservation) {
        return (reservation.getStartTagAt().isBefore(reservation.getStartAt().plusMinutes(5)) && reservation.getStartTagAt().isAfter(reservation.getStartAt()))
                || reservation.getStartTagAt().isEqual(reservation.getStartAt().plusMinutes(5));
    }

    private boolean isEndTagInTime(Reservation reservation, LocalDateTime endTagAt) {
        return (endTagAt.isBefore(reservation.getEndAt().plusMinutes(5)) && endTagAt.isAfter(reservation.getEndAt()))
                || endTagAt.isEqual(reservation.getEndAt().plusMinutes(5));
    }
}
