package com.newfit.reservation.service.routine;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.response.RoutineDetailEquipmentResponse;
import com.newfit.reservation.dto.response.RoutineDetailResponse;
import com.newfit.reservation.dto.response.RoutineListResponse;
import com.newfit.reservation.dto.response.RoutineResponse;
import com.newfit.reservation.repository.equipment.EquipmentRepository;
import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import com.newfit.reservation.repository.routine.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Routine registerRoutine(Authority authority, String routineName) {
        if(validateDuplicate(authority, routineName))
            throw new IllegalArgumentException();

        return routineRepository.save(Routine.createRoutine(authority, routineName));
    }

    // id를 통해 Routine 객체를 조회합니다.
    public Routine findById(Long routineId) {
        return routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);
    }

    /*
    Routine의 이름을 업데이트하는 메소드입니다.
     */
    public void updateRoutine(Long routineId, String routineName) {
        Routine findRoutine = routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);
        findRoutine.updateName(routineName);
    }

    // 특정 User의 Authority가 생성한 모든 Routine을 조회하고 Dto로 변환하여 반환합니다.
    public RoutineListResponse findAllRoutinesByAuthority(Authority authority) {
        List<Routine> findRoutines = routineRepository.findAllByAuthority(authority);

        List<RoutineResponse> routines = findRoutines.stream()
                .map(RoutineResponse::new)
                .collect(Collectors.toList());

        return RoutineListResponse.createResponse(routines);
    }

    /*
    Routine 객체를 삭제하는 메소드입니다. EquipmentRoutine 객체는 무조건 Routine 객체에 종속되므로
    특정 Routine 객체를 삭제하는 경우 해당 Routine에 묶여있는 EquipmentRoutine 객체들도 모두 삭제합니다.
     */
    public void deleteRoutine(Long routineId) {
        Routine findRoutine = routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);
        equipmentRoutineRepository.deleteAllByRoutine(findRoutine);
        routineRepository.deleteById(routineId);
    }

    /*
    Routine id를 받아서 해당 Routine에 묶인 EquipmentRoutine을 조회합니다.
    조회한 EquipmentRoutine들을 순회하며 Equipment를 조회합니다.
    조회한 Equipment들을 RoutineDetailEquipmentResponse Dto로 변환하고 
    Routine 정보와 함께 RoutineDetailResponse를 구성하여 반환합니다.
     */
    public RoutineDetailResponse findRoutineDetail(Long routineId) {
        Routine findRoutine = routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);

        List<EquipmentRoutine> findEquipmentRoutines = equipmentRoutineRepository.findAllByRoutine(findRoutine);

        List<Equipment> findEquipments = findEquipmentRoutines.stream()
                .map(EquipmentRoutine::getEquipment)
                .collect(Collectors.toList());

        List<RoutineDetailEquipmentResponse> equipments = findEquipments.stream()
                .map(RoutineDetailEquipmentResponse::new)
                .collect(Collectors.toList());

        return RoutineDetailResponse.builder()
                .routineId(findRoutine.getId())
                .routineName(findRoutine.getName())
                .equipments(equipments)
                .build();
    }

    // 해당 User의 Authority가 이전에 등록한 Routine중에 동일한 이름이 있는지 확인합니다.
    private boolean validateDuplicate(Authority authority, String routineName) {
        return routineRepository.findByAuthorityAndName(authority, routineName).isPresent();
    }
}
