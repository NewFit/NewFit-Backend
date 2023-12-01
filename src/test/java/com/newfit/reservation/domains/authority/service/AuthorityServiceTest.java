package com.newfit.reservation.domains.authority.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.common.exception.ErrorCodeType;
import com.newfit.reservation.domains.authority.domain.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthorityServiceTest {

    @Autowired
    AuthorityService authorityService;

    @Test
    void authority_중복_등록_validation() {
        // given
        Long authorityId = 1L;

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // DB에서 ADMIN authority 하나 조회
        Authority findAuthority = authorityService.findById(authorityId);
        Long userId = findAuthority.getUser().getId();
        Long gymId = findAuthority.getGym().getId();

        // 위에서 조회해온 authority의 User와 Gym을 이용하여 RoleType이 USER인 테스트 데이터를 DB에 삽입
        authorityService.register(userId, gymId, mockResponse);

        // when, then

        // 위에서 삽입한 데이터를 다시 삽입하려고 할 시 CustomException이 발생해야 한다
        CustomException exception = assertThrows(CustomException.class,
                () -> authorityService.register(userId, gymId, mockResponse));
        assertThat(exception.getErrorCodeType()).isEqualTo(ErrorCodeType.DUPLICATE_AUTHORITY_REQUEST);
    }
}