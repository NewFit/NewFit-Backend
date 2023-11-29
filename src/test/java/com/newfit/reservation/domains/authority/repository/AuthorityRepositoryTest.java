package com.newfit.reservation.domains.authority.repository;

import com.newfit.reservation.common.config.QuerydslConfig;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.domain.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void userId로_authority_모두_조회() {
        // given
        Long userId = 1L;

        // when
        List<Authority> result = authorityRepository.findAllAuthorityByUserId(userId);

        // then
        for (Authority authority : result) {
            assertThat(authority.getUser().getId()).isEqualTo(userId);
        }
    }

    @Test
    void userId_GymId_RoleType_으로_하나_조회() {
        // given
        Long userId = 1L;
        Long gymId = 1L;
        RoleType roleType = RoleType.ADMIN;

        // when
        Authority result = authorityRepository.findOneByUserIdAndGymIdAndRoleType(userId, gymId, roleType);

        // then
        assertThat(result.getUser().getId()).isEqualTo(userId);
        assertThat(result.getGym().getId()).isEqualTo(gymId);
        assertThat(result.getRoleType()).isEqualTo(roleType);
    }

    @Test
    void creditAcquisition이_모두_0이_아닌지_확인() {
        // when
        List<Authority> authorityList = authorityRepository.findAllByCreditAcquisitionCountNotZero();

        // then
        for (Authority authority : authorityList) {
            assertThat(authority.getCreditAcquisitionCount()).isNotEqualTo(0);
        }
    }

    @Test
    void nickname과_gymId로_하나_조회() {
        // given
        String nickname = "Sponge Bob";
        Long gymId = 1L;

        // when
        Authority result = authorityRepository.findOneByUserNicknameAndGymId(nickname, gymId)
                .orElse(null);

        // then
        assertThat(result.getUser().getNickname()).isEqualTo(nickname);
        assertThat(result.getGym().getId()).isEqualTo(gymId);
    }
}