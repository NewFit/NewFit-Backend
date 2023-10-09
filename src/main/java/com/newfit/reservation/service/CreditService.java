package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.dto.response.CreditRanking;
import com.newfit.reservation.dto.response.UserRankInfo;
import com.newfit.reservation.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.newfit.reservation.exception.ErrorCode.AUTHORITY_NOT_FOUND;

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

        List<CreditRanking> creditList = creditRepository
                .findAllByGymIdAndYearAndMonth(authority.getGym().getId(), (short) now.getYear(), (short) now.getMonthValue());

        List<UserRankInfo> rankingList = creditList.subList(0, 10)
                .stream()
                .map(UserRankInfo::new)
                .toList();

        UserRankInfo userRankInfo = creditList.stream()
                .filter(c -> c.getAuthority().equals(authority))
                .map(UserRankInfo::new)
                .findAny().orElseGet(() -> UserRankInfo.noCredit(authority));

        return new UserRankInfoListResponse(authority.getGym().getName(), rankingList, userRankInfo);
    }
}
