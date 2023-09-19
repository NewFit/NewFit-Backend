package com.newfit.reservation.service.routine;

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
import com.newfit.reservation.repository.routine.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentRoutineService {

    private final EquipmentRoutineRepository equipmentRoutineRepository;
    private final EquipmentRepository equipmentRepository;
    private final RoutineRepository routineRepository;

    /*
    Controller로부터 Routine, RoutineEquipmentRequest의 리스트를 넘겨받아
    새로운 EquipmentRoutine 객체를 생성 및 저장합니다. Duration 클래스의 static 메소드를 이용하여
    Long 타입의 데이터를 Duration 타입으로 변환할 수 있습니다.
     */
    public void registerEquipmentRoutine(Routine routine,
                                         List<RoutineEquipmentRequest> routineRequests) {

        List<Short> sequences = routineRequests.stream()
                .map(RoutineEquipmentRequest::getSequence)
                .collect(Collectors.toList());

        // valid한 sequence 값들로 이루어져 있는지 체크합니다.
        checkSequence(sequences);

        for (RoutineEquipmentRequest routineRequest : routineRequests) {
            Equipment equipment = equipmentRepository.findById(routineRequest.getEquipmentId())
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
    특정 Routine에 묶인 EquipmentRoutine 객체를 업데이트하는 메소드입니다.
    1. 기존 EquipmentRoutine 객체를 삭제
    2. 기존 EquipmentRoutine 객체를 수정
    3. 새로운 EquipmentRoutine 객체를 등록
     */
    public void updateEquipmentRoutinesInRoutine(Routine routine, UpdateRoutineRequest requestDto) {

        // Routine의 모든 EquipmentRoutine 객체를 조회합니다.
        List<EquipmentRoutine> allByRoutine = equipmentRoutineRepository.findAllByRoutine(routine);
        // 기존 sequence List
        List<Short> currentSequences = extractCurrentSequences(allByRoutine);

        // patch 로직 실행 후의 sequence를 생성하기 위한 등록, 삭제, 수정할 sequence 정보
        List<Short> addSequences = extractAddSequences(requestDto);
        List<Short> removeSequences = extractRemoveSequences(requestDto, routine);
        Map<Short, Short> sequenceMap = generateSequenceMap(requestDto, routine);

        // patch 로직 실행 후의 sequence를 생성 후 validition check
        List<Short> resultSequences = getResultSequences(currentSequences, removeSequences, addSequences, sequenceMap);
        checkSequence(resultSequences);

        // patch 로직 실행 => 삭제, 수정, 새로운 기구 등록 순
        removeEquipmentRoutineInRoutine(requestDto, allByRoutine);
        modifyEquipmentRoutineInRoutine(requestDto, allByRoutine);
        addEquipRoutineInRoutine(routine, requestDto);
    }

    private void addEquipRoutineInRoutine(Routine routine, UpdateRoutineRequest requestDto) {
        if (!requestDto.getAddEquipments().isEmpty()){
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

    private void modifyEquipmentRoutineInRoutine(UpdateRoutineRequest requestDto, List<EquipmentRoutine> allByRoutine) {
        if (!requestDto.getUpdateEquipments().isEmpty()) {
            List<UpdateEquipmentRequest> updateEquipments = requestDto.getUpdateEquipments();

            for (UpdateEquipmentRequest updateEquipment : updateEquipments) {

                // 수정 사항을 반영할 EquipmentRoutine 객체를 allByRoutine에서 추출합니다.
                EquipmentRoutine equipmentRoutine = extractUpdateTarget(updateEquipment, allByRoutine);

                // 순서를 수정 시 실행.
                if (updateEquipment.getSequence() != null) {
                    equipmentRoutine.updateSequence(updateEquipment.getSequence());
                }
                // 운동시간을 수정 시 실행.
                if (updateEquipment.getDuration() != null) {
                    equipmentRoutine.updateDuration(Duration.ofMinutes(updateEquipment.getDuration()));
                }
            }
        }
    }

    private void removeEquipmentRoutineInRoutine(UpdateRoutineRequest requestDto, List<EquipmentRoutine> allByRoutine) {
        if (!requestDto.getRemoveEquipments().isEmpty()) {

            // allByRoutine에서 제거 대상인 EquipmentRoutine List
            List<EquipmentRoutine> removeTargets = extractRemoveTargets(requestDto, allByRoutine);

            // 추출한 EquipmentRoutine 객체들을 모두 삭제
            equipmentRoutineRepository.deleteAll(removeTargets);
        }
    }

    private EquipmentRoutine extractUpdateTarget(UpdateEquipmentRequest updateEquipment, List<EquipmentRoutine> allByRoutine) {
        Long targetId = updateEquipment.getEquipmentId();

        return allByRoutine.stream()
                .filter(equipmentRoutine -> equipmentRoutine.getEquipment().getId().equals(targetId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<EquipmentRoutine> extractRemoveTargets(UpdateRoutineRequest requestDto, List<EquipmentRoutine> allByRoutine) {
        // 사용자가 Routine에서 제거한 Equipment들의 id List
        List<Long> equipmentIdList = requestDto.getRemoveEquipments().stream()
                .map(RemoveEquipmentRequest::getEquipmentId)
                .collect(Collectors.toList());

        // allByRoutine을 순회하며 equipmentIdList에 들어있는 값과 대응되는 EquipmentRoutine List
        return allByRoutine.stream()
                .filter(equipmentRoutine -> equipmentIdList.contains(equipmentRoutine.getEquipment().getId()))
                .collect(Collectors.toList());
    }

    private List<Short> extractCurrentSequences(List<EquipmentRoutine> allByRoutine) {
        return allByRoutine.stream()
                .map(EquipmentRoutine::getSequence)
                .collect(Collectors.toList());
    }

    private List<Short> extractAddSequences(UpdateRoutineRequest requestDto) {
        return requestDto.getAddEquipments().stream()
                .map(AddEquipmentRequest::getSequence)
                .collect(Collectors.toList());
    }

    private List<Short> extractRemoveSequences(UpdateRoutineRequest requestDto, Routine routine) {
        List<EquipmentRoutine> equipmentRoutines = requestDto.getRemoveEquipments().stream()
                .map(r -> equipmentRoutineRepository.findByEquipmentIdAndRoutine(r.getEquipmentId(), routine)
                        .orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        return equipmentRoutines.stream()
                .map(EquipmentRoutine::getSequence)
                .collect(Collectors.toList());
    }

    private Map<Short, Short> generateSequenceMap(UpdateRoutineRequest requestDto, Routine routine) {
        Map<Short, Short> sequenceMap = new HashMap<>();
        List<UpdateEquipmentRequest> updateEquipments = requestDto.getUpdateEquipments();

        for (UpdateEquipmentRequest updateEquipmentRequest : updateEquipments) {
            EquipmentRoutine equipmentRoutine = equipmentRoutineRepository
                    .findByEquipmentIdAndRoutine(updateEquipmentRequest.getEquipmentId(), routine)
                    .orElseThrow(IllegalArgumentException::new);

            sequenceMap.put(equipmentRoutine.getSequence(), updateEquipmentRequest.getSequence());
        }
        return sequenceMap;
    }

    private List<Short> getResultSequences(List<Short> currentSequences, List<Short> removeSequences,
                                           List<Short> addSequences, Map<Short, Short> sequenceMap) {
        // remove
        currentSequences.removeAll(removeSequences);

        // update
        currentSequences.removeAll(sequenceMap.keySet());
        currentSequences.addAll(sequenceMap.values());

        // add
        currentSequences.addAll(addSequences);

        return currentSequences;
    }

    private void checkSequence(List<Short> sequences) {
        Collections.sort(sequences);

        /*
        range 메소드는 exclusive upper bound 사용하므로 모든 원소를 순회하려면 sequences.size()가 들어가야함.
        하지만 allMatch 메소드를 사용하므로 sequences 리스트의 가장 마지막 원소는 포함되면 안 됨.
        따라서 sequences.size() - 1을 넣었음.
         */
        boolean result = (sequences.get(0) == 0)
                && (IntStream.range(0, sequences.size() - 1)
                .allMatch(i -> sequences.get(i + 1) == sequences.get(i) + 1));

        if(!result) throw new IllegalArgumentException("잘못된 sequence 값입니다.");
    }

    //
    public List<EquipmentRoutine> findAllByRoutineId(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(IllegalArgumentException::new);
        return equipmentRoutineRepository.findAllByRoutineOrderBySequence(routine);
    }

}
