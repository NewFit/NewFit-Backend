package com.newfit.reservation.domains.gym.repository;

import com.newfit.reservation.common.config.QuerydslConfig;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.dto.request.admin.CreateGymRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GymRepositoryTest {

    @Autowired
    private GymRepository gymRepository;

    @Test
    @DisplayName("영어로 된 헬스장 이름 조회 시 대소문자를 구분하지 않고 조회해야 한다.")
    void caseInsensitiveSearchTest() {
        // given
        CreateGymRequest request = new CreateGymRequest();
        request.setName("Sad Gym");
        request.setTel("010-1235-4565");
        request.setAddress("마포구 와우산로");
        request.setOpenAt(LocalTime.MIN);
        request.setCloseAt(LocalTime.MAX);
        request.setAllDay(false);

        Gym gym = Gym.from(request);
        gymRepository.save(gym);

        // when
        List<Gym> gyms = gymRepository.findAllByNameContaining(Arrays.asList("sAd", "gym"));

        // then
        assertThat(gyms.size()).isEqualTo(1);
    }

    @Test
    void 헬스장_키워드로_검색() {
        // given
        CreateGymRequest request = new CreateGymRequest();
        request.setName("Sad but happy Gym");
        request.setTel("010-1235-4565");
        request.setAddress("마포구 와우산로");
        request.setOpenAt(LocalTime.MIN);
        request.setCloseAt(LocalTime.MAX);
        request.setAllDay(false);

        Gym gym = Gym.from(request);
        gymRepository.save(gym);

        // when
        List<Gym> findBySad = gymRepository.findAllByNameContaining(List.of("Sad"));
        List<Gym> findByHappy = gymRepository.findAllByNameContaining(List.of("Happy"));

        // then
        assertThat(findBySad.size()).isEqualTo(findByHappy.size());
    }
}