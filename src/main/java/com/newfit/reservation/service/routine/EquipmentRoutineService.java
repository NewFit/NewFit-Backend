package com.newfit.reservation.service.routine;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.dto.request.RoutineEquipmentRequest;
import com.newfit.reservation.dto.request.UpdateRoutineRequest;
import com.newfit.reservation.dto.request.routine.AddEquipmentRequest;
import com.newfit.reservation.dto.request.routine.RemoveEquipmentRequest;
import com.newfit.reservation.dto.request.routine.UpdateEquipmentRequest;
import com.newfit.reservation.repository.equipment.EquipmentRepository;
import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

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
                            .sequence(routineRequest.getSequence())
                            .build());
        }
    }

    /*
    특정 Routine에 묶인 EquipmentRoutine 객체를 업데이트하는 메소드입니다. 수행되는 작업은 3가지입니다.
    1. 기존에 있던 EquipmentRoutine 객체를 삭제합니다.
    2. 기존에 있던 EquipmentRoutine 객체를 수정합니다.
    3. 새로운 EquipmentRoutine 객체를 생성하여 Routine 객체에 연관시킵니다.
     */
    public void updateEquipmentRoutinesInRoutine(Routine routine, UpdateRoutineRequest requestDto) {

        // 먼저 넘겨 받은 Routine 객체에 묶여있는 모든 EquipmentRoutine 객체를 조회합니다.
        List<EquipmentRoutine> allByRoutine = equipmentRoutineRepository.findAllByRoutine(routine);

        // 만약 사용자가 Rotine에서 제거한 기구가 있다면 실행됩니다.
        if (requestDto.getRemoveEquipments() != null) {

            // 사용자가 Routine에서 제거한 Equipment들의 id를 Dto로부터 리스트 형태로 추출합니다.
            List<Long> equipmentIdList = requestDto.getRemoveEquipments().stream()
                    .map(RemoveEquipmentRequest::getEquipmentId)
                    .collect(Collectors.toList());

            // allByRoutine을 순회하며 equipmentIdList에 들어있는 값과 대응되는 EquipmentRoutine 객체들을 추출합니다.
            List<EquipmentRoutine> removeTargets = allByRoutine.stream()
                    .filter(equipmentRoutine -> equipmentIdList.contains(equipmentRoutine.getEquipment().getId()))
                    .collect(Collectors.toList());

            // 추출한 EquipmentRoutine 객체들을 모두 삭제합니다.
            equipmentRoutineRepository.deleteAll(removeTargets);
        }

        // 만약 사용자가 Routine에서 수정한 사항이 있다면 실행됩니다.
        if (requestDto.getUpdateEquipments() != null) {
            List<UpdateEquipmentRequest> updateEquipments = requestDto.getUpdateEquipments();

            for (UpdateEquipmentRequest updateEquipment : updateEquipments) {

                // 수정 사항을 반영할 EquipmentRoutine 객체를 allByRoutine에서 추출합니다.
                EquipmentRoutine findEquipmentRoutine = allByRoutine.stream()
                        .filter(equipmentRoutine -> equipmentRoutine.getEquipment().getId()
                                .equals(updateEquipment.getEquipmentId()))
                        .findFirst().orElseThrow(IllegalArgumentException::new);

                // 만약 순서를 수정했다면 실행됩니다.
                if (updateEquipment.getSequence() != null) {
                    findEquipmentRoutine.updateSequence(updateEquipment.getSequence());
                }
                // 만약 운동시간을 수정했다면 실행됩니다.
                if (updateEquipment.getDuration() != null) {
                    findEquipmentRoutine.updateDuration(Duration.ofMinutes(updateEquipment.getDuration()));
                }
            }
        }

        // 만약 사용자가 Routine에 새로운 기구를 등록했다면 실행됩니다.
        if (requestDto.getAddEquipments() != null){
            List<AddEquipmentRequest> addEquipments = requestDto.getAddEquipments();

            for (AddEquipmentRequest addEquipment : addEquipments) {
                Equipment equipment = equipmentRepository.findById(addEquipment.getEquipmentId())
                        .orElseThrow(IllegalArgumentException::new);

                equipmentRoutineRepository.save(
                        EquipmentRoutine.builder()
                                .equipment(equipment)
                                .routine(routine)
                                .duration(Duration.ofMinutes(addEquipment.getDuration()))
                                .sequence(addEquipment.getSequence())
                                .build());
            }
        }
    }
}
