package com.newfit.reservation.service.routine;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.request.RoutineEquipmentRequest;
import com.newfit.reservation.repository.equipment.EquipmentRepository;
import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentRoutineService {

    private final EquipmentRoutineRepository equipmentRoutineRepository;
    private final EquipmentRepository equipmentRepository;

    /*
    Controller로부터 Gym, Routine, RoutineEquipmentRequest의 리스트를 넘겨받아
    새로운 EquipmentRoutine 객체를 생성 및 저장합니다. Duration 클래스의 static 메소드를 이용하여
    Long 타입의 데이터를 Duration 타입으로 변환할 수 있습니다.
     */
    public void registerEquipmentRoutine(Gym gym, Routine routine,
                                         List<RoutineEquipmentRequest> routineRequests) {

        for (RoutineEquipmentRequest routineRequest : routineRequests) {
            Equipment equipment = equipmentRepository.findByIdAndGym(routineRequest.getEquipmentId(), gym)
                    .orElseThrow(IllegalArgumentException::new);

            equipmentRoutineRepository.save(
                    EquipmentRoutine.builder()
                    .equipment(equipment)
                    .routine(routine)
                    .duration(Duration.ofMinutes(routineRequest.getDuration()))
                    .build());
        }
    }
}
