package com.newfit.reservation.domains.routine.service;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.repository.EquipmentRepository;
import com.newfit.reservation.domains.routine.domain.EquipmentRoutine;
import com.newfit.reservation.domains.routine.domain.Routine;
import com.newfit.reservation.domains.routine.dto.request.EquipmentRoutineRequest;
import com.newfit.reservation.domains.routine.dto.request.RoutineEquipmentRequest;
import com.newfit.reservation.domains.routine.dto.request.UpdateEquipmentRoutineRequest;
import com.newfit.reservation.domains.routine.repository.EquipmentRoutineRepository;
import com.newfit.reservation.domains.routine.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

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
			.map(RoutineEquipmentRequest::getSequence).collect(Collectors.toList());

		// valid한 sequence 값들로 이루어져 있는지 체크합니다.
		checkSequence(sequences);

		for (RoutineEquipmentRequest routineRequest : routineRequests) {
			Equipment equipment = equipmentRepository.findById(routineRequest.getEquipmentId())
				.orElseThrow(() -> new CustomException(EQUIPMENT_NOT_FOUND));

			equipmentRoutineRepository.save(EquipmentRoutine.createEquipmentRoutine(equipment, routine,
				Duration.ofMinutes(routineRequest.getDuration()), routineRequest.getSequence()));
		}
	}

	public void updateEquipmentRoutinesInRoutine(Long routineId, UpdateEquipmentRoutineRequest request) {
		List<EquipmentRoutine> existingRoutine = equipmentRoutineRepository.findAllByRoutineIdOrderBySequence(
			routineId);
		List<EquipmentRoutineRequest> afters = request.getEquipments();

		processCommonSequence(existingRoutine, afters);
		processSurplusSequence(request, routineId, existingRoutine, afters);
	}

	private void processCommonSequence(List<EquipmentRoutine> existingRoutine, List<EquipmentRoutineRequest> afters) {
		int min = Integer.min(existingRoutine.size(), afters.size());
		IntStream.range(0, min)
			.filter(i -> !existingRoutine.get(i).isSameWithUpdateRequest(afters.get(i)))
			.forEach(i -> {
				EquipmentRoutine before = existingRoutine.get(i);
				EquipmentRoutineRequest after = afters.get(i);
				Equipment equipment = equipmentRepository.findById(after.getEquipmentId())
					.orElseThrow(() -> new CustomException(EQUIPMENT_NOT_FOUND));
				before.updateEquipment(equipment);
				before.updateDuration(Duration.ofMinutes(after.getDuration()));
			});
	}

	private void processSurplusSequence(UpdateEquipmentRoutineRequest request, Long routineId,
		List<EquipmentRoutine> existingRoutine, List<EquipmentRoutineRequest> afters) {
		int min = Integer.min(existingRoutine.size(), request.getEquipmentsCount());
		if (existingRoutine.size() > request.getEquipmentsCount()) {
			equipmentRoutineRepository.deleteAll(existingRoutine.subList(min, existingRoutine.size()));
		} else if (existingRoutine.size() < request.getEquipmentsCount()) {
			List<EquipmentRoutineRequest> toSave = afters.subList(min, request.getEquipmentsCount());
			Routine routine = routineRepository.findById(routineId)
				.orElseThrow(() -> new CustomException(ROUTINE_NOT_FOUND));
			appendEquipmentToRoutine(routine, toSave, min);
		}
	}

	private void appendEquipmentToRoutine(Routine routine, List<EquipmentRoutineRequest> toSave,
		Integer sequenceStart) {
		AtomicInteger sequence = new AtomicInteger(sequenceStart);

		List<EquipmentRoutine> saveList = toSave.stream()
			.map(equipmentRequest -> {
				Equipment equipment = equipmentRepository.findById(equipmentRequest.getEquipmentId())
					.orElseThrow(() -> new CustomException(EQUIPMENT_NOT_FOUND));
				return EquipmentRoutine.createEquipmentRoutine(equipment, routine,
					Duration.ofMinutes(equipmentRequest.getDuration()), (short)sequence.incrementAndGet());
			}).toList();
		equipmentRoutineRepository.saveAll(saveList);
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

		if (!result)
			throw new CustomException(INVALID_SEQUENCE);
	}
}
