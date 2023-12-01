package com.newfit.reservation.domains.equipment.repository;

import com.newfit.reservation.common.config.QuerydslConfig;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.repository.GymRepository;
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
class EquipmentGymRepositoryTest {

    @Autowired
    private EquipmentGymRepository equipmentGymRepository;

    @Autowired
    private GymRepository gymRepository;

    @Test
    void gymId와_PurposeType으로_조회() {
        // given
        Long gymId = 1L;
        Gym gym = gymRepository.findById(gymId).orElseThrow();
        PurposeType purpose= PurposeType.LOWER_BODY;
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);
        List<EquipmentGym> equipmentGyms = allByGym.stream()
                .filter(equipmentGym -> equipmentGym.getEquipment().getPurposeType().equals(purpose))
                .toList();

        // when
        List<EquipmentGym> result = equipmentGymRepository.findAllByGymAndPurpose(gymId, purpose);

        // then
        assertThat(result).isEqualTo(equipmentGyms);
    }
}