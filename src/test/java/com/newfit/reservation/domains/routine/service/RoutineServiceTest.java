package com.newfit.reservation.domains.routine.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newfit.reservation.domains.routine.domain.Routine;

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
}