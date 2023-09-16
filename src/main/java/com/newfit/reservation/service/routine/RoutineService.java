package com.newfit.reservation.service.routine;


import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.request.RoutineEquipmentRequest;
import com.newfit.reservation.dto.response.RoutineListResponse;
import com.newfit.reservation.dto.response.RoutineResponse;
import com.newfit.reservation.repository.equipment.EquipmentRepository;
import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import com.newfit.reservation.repository.routine.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final EquipmentRoutineRepository equipmentRoutineRepository;
    private final EquipmentRepository equipmentRepository;

    /*
    이전에 등록한 루틴과 동일한 이름이라면 exception이 발생합니다.
    아니라면 새로운 Routine을 등록하고 그 Routine을 반환합니다.
     */
    public Routine registerRoutine(User user, String routineName) {
        if(validateDuplicate(user, routineName))
            throw new IllegalArgumentException();

        return routineRepository.save(Routine.builder()
                .user(user)
                .name(routineName)
                .build());
    }

    // id를 통해 Routine 객체를 조회합니다.
    public Routine findById(Long routineId) {
        return routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);
    }

    /*
    실질적으로 Routine을 업데이트하는 메소드입니다. 우선 해당 Routine에 묶여있는 EquipmentRoutine 객체를 모두 삭제하고
    Dto를 통해 넘겨받은 데이터를 바탕으로 EquipmentRoutine 객체를 새로 생성하여 등록합니다. 실질적으론 삭제 후 재등록 과정입니다.
     */
    public void updateRoutine(Gym gym, Routine routine, List<RoutineEquipmentRequest> routineRequests) {
        equipmentRoutineRepository.deleteAllByRoutine(routine);

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

    // 특정 User가 생성한 모든 Routine을 조회하고 Dto로 변환하여 반환합니다.
    public RoutineListResponse getAllRoutinesByUser(User user) {
        List<Routine> findRoutines = routineRepository.findAllByUser(user);

        List<RoutineResponse> routines = findRoutines.stream()
                .map(RoutineResponse::new)
                .collect(Collectors.toList());

        return RoutineListResponse.builder()
                .routines(routines)
                .build();
    }

    // 해당 유저가 이전에 등록한 Routine중에 동일한 이름이 있는지 확인합니다.
    private boolean validateDuplicate(User user, String routineName) {
        return routineRepository.findByUserAndName(user, routineName).isPresent();
    }
}
