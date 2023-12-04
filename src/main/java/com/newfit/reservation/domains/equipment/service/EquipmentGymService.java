package com.newfit.reservation.domains.equipment.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.equipment.domain.ConditionType;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentResponse;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.IntStream;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentGymService {

    private final EquipmentGymRepository equipmentGymRepository;

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
    Gym으로 EquipmentGym을 모두 조회
    Response를 위한 Dto에 담아 반환
     */
    public EquipmentGymListResponse findAllInGym(Gym gym) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);

        List<EquipmentResponse> equipmentResponses = allByGym.stream()
                .map(EquipmentResponse::new).toList();

        return EquipmentGymListResponse.createResponse(gym.getName(), equipmentResponses);
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

    public EquipmentGymListResponse findAllInGymByPurpose(Gym gym, PurposeType purposeType) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGymAndPurpose(gym.getId(), purposeType);
        List<EquipmentResponse> equipmentResponses = allByGym.stream()
                .map(EquipmentResponse::new).toList();

        return EquipmentGymListResponse.createResponse(gym.getName(), equipmentResponses);
    }

    public EquipmentGymListResponse findAllInGymByEquipment(Gym gym, Equipment equipment) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGymAndEquipment(gym, equipment);
        List<EquipmentResponse> equipmentResponses = allByGym.stream()
                .map(EquipmentResponse::new).toList();

        return EquipmentGymListResponse.createResponse(gym.getName(), equipmentResponses);
    }

    /*
    EquipmentGymId로 EquipmentGym 조회
     */
    public EquipmentGym findOneById(Long equipmentGymId) {
        return equipmentGymRepository
                .findById(equipmentGymId)
                .orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));
    }
}
