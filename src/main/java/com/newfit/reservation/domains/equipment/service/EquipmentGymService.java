package com.newfit.reservation.domains.equipment.service;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.equipment.domain.ConditionType;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.dto.request.EquipmentQueryRequest;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentResponse;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.gym.domain.Gym;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentGymService {

	private final EquipmentGymRepository equipmentGymRepository;
	private final AuthorityRepository authorityRepository;

	/*
	입력받은 개수(count)만큼 등록
	 */
	public void registerEquipmentInGym(Equipment equipment, Gym gym, Integer count, List<String> equipmentGymNames) {
		if (equipmentGymNames.size() < count)
			throw new CustomException(INCOMPATIBLE_EQUIPMENT_NAME_COUNT);

		IntStream.range(0, count)
			.forEach(repeat -> equipmentGymRepository
				.save(EquipmentGym.createEquipmentGym(equipment, gym, equipmentGymNames.get(repeat))));
	}

	/*
	EquipmentGym의 Condition을 수정
	 */
	public void updateCondition(Long equipmentGymId, ConditionType conditionType) {
		EquipmentGym equipmentGym = findOneById(equipmentGymId);
		equipmentGym.updateCondition(conditionType);
	}

	/*
	equipmentGym 삭제
	 */
	public void deactivateEquipmentGym(Long equipmentGymId) {
		EquipmentGym equipmentGym = findOneById(equipmentGymId);
		equipmentGym.deactivate();
	}

	/*
	EquipmentGymId로 EquipmentGym 조회
	 */
	public EquipmentGym findOneById(Long equipmentGymId) {
		return equipmentGymRepository
			.findById(equipmentGymId)
			.orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public EquipmentGymListResponse findAllByQuery(Long authorityId, EquipmentQueryRequest request) {
		Gym gym = getGymByAuthorityId(authorityId);

		List<EquipmentGym> allByGym = equipmentGymRepository.findAllByQueryOption(gym, request);

		List<EquipmentResponse> equipmentResponses = allByGym.stream()
			.map(EquipmentResponse::new)
			.toList();

		return EquipmentGymListResponse.createResponse(gym.getName(), equipmentResponses);
	}

	private Gym getGymByAuthorityId(Long authorityId) {
		Authority authority = authorityRepository.findById(authorityId)
			.orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

		return authority.getGym();
	}
}
