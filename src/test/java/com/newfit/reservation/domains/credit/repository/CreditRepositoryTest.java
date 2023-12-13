package com.newfit.reservation.domains.credit.repository;

import com.newfit.reservation.common.config.QuerydslConfig;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.credit.dto.response.CreditRanking;
import com.newfit.reservation.domains.credit.dto.response.UserRankInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


    @Test
    void findTop10ByGymIdAndYearAndMonth의_쿼리_syntax() {
        // given
        Long gymId = 1L;
        LocalDateTime now = now();

        // when
        List<CreditRanking> creditList = creditRepository
                .findTop10ByGymIdAndYearAndMonth(gymId, (short) now.getYear(), (short) now.getMonthValue());

        // then
        assertThat(creditList).isNotNull();
    }

    @Test
    void findRank의_쿼리_syntax() {
        // given
        Long authorityId = 1L;
        Long gymId = 1L;
        LocalDateTime now = now();
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(IllegalArgumentException::new);
        // when
        UserRankInfo userRankInfo = creditRepository
                .findRank(authority.getId(),
                        gymId,
                        (short) now.getYear(),
                        (short) now.getMonthValue())
                .map(UserRankInfo::new)
                .orElseGet(() -> UserRankInfo.noCredit(authority));

        // then
        assertThat(userRankInfo).isNotNull();
    }
}