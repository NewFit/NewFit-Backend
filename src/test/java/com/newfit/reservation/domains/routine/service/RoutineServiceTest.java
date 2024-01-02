package com.newfit.reservation.domains.routine.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newfit.reservation.domains.routine.domain.Routine;
import com.newfit.reservation.domains.routine.dto.response.RoutineDetailResponse;

@SpringBootTest
@Transactional
class RoutineServiceTest {

	@Autowired
	RoutineService routineService;

	@Test
	void 루틴의_이름을_수정() {
		// given
		Long routineId = 2L;
		String newRoutineName = "변경된 이름";

		// when
		Routine routine = routineService.findById(routineId);
		routine.updateName(newRoutineName);

		// then
		assertThat(routine.getName()).isEqualTo(newRoutineName);
	}

	@Test
	void 루틴의_상세정보_조회시_count와_실제_개수가_일치하는가() {
		// given
		Long routineId = 2L;

		// when
		RoutineDetailResponse response = routineService.findRoutineDetail(routineId);

		// then
		assertThat(response.getEquipments().size()).isEqualTo(response.getEquipmentsCount());
	}
}