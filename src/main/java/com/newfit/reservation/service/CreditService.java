package com.newfit.reservation.service;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import com.newfit.reservation.dto.response.UserRankInfo;
import com.newfit.reservation.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditService {

    private final CreditRepository creditRepository;
    private final AuthorityRepository authorityRepository;

    public UserRankInfoListResponse getRankInGym(Long authorityId) {
        LocalDateTime now = LocalDateTime.now();
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(IllegalArgumentException::new);
        List<UserRankInfo> rankingList = new ArrayList<>();

        Pageable topTen = PageRequest.of(0,10);
        List<Credit> creditList = creditRepository.findAllByGymAndYearAndMonth(authority.getGym(), (short) now.getYear(), (short) now.getMonthValue(), topTen).stream().toList();
        creditList.forEach((credit -> rankingList.add(new UserRankInfo(credit, getRank(rankingList, credit)))));
        return new UserRankInfoListResponse(authority.getGym().getName(), rankingList);
    }

    private Long getRank(List<UserRankInfo> rankingList , Credit credit) {
        if (rankingList.isEmpty()) {
            return 1L;
        }

        // credit 동점자 순위 처리
        int indexOfLastElement = rankingList.size() - 1;
        if (rankingList.get(indexOfLastElement).getAmount().equals(credit.getAmount())) {
            return rankingList.get(indexOfLastElement).getRank();
        }
        else {
            return rankingList.get(indexOfLastElement).getRank() +1L;
        }
    }
}
