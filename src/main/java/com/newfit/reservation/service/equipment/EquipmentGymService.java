package com.newfit.reservation.service.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.equipment.Purpose;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.dto.response.EquipmentResponse;
import com.newfit.reservation.exception.CustomException;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.IntStream;

import static com.newfit.reservation.exception.ErrorCode.*;

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

        return new EquipmentGymListResponse(gym.getName(), allByGym.size(), equipmentResponses);
    }

    /*
    EquipmentGym의 Condition을 수정
     */
    public void updateCondition(Long equipmentGymId, Condition condition) {
        EquipmentGym equipmentGym = findOneById(equipmentGymId);
        equipmentGym.updateCondition(condition);
    }

    /*
    equipmentGym 삭제
     */
    public void deleteEquipmentGym(Long equipmentGymId) {
        equipmentGymRepository.deleteById(equipmentGymId);
    }

    /*
    gym과 purpose로 EquipmentGymList 조회
     */
    public EquipmentGymListResponse findAllInGymByPurpose(Gym gym, Purpose purpose) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);
        List<EquipmentGym> allByGymAndPurpose = allByGym
                .stream()
                .filter(equipmentGym -> equipmentGym.getEquipment().getPurpose().equals(purpose)).toList();

        List<EquipmentResponse> equipmentResponses = allByGymAndPurpose.stream()
                .map(EquipmentResponse::new).toList();

        return new EquipmentGymListResponse(gym.getName(), allByGymAndPurpose.size(), equipmentResponses);
    }

    /*
    gym과 equipment로 EquipmentGymList 조회
     */
    public EquipmentGymListResponse findAllInGymByEquipment(Gym gym, Equipment equipment) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);
        List<EquipmentGym> allByGymAndEquipment = allByGym.stream()
                .filter(equipmentGym -> equipmentGym.getEquipment().equals(equipment)).toList();

        List<EquipmentResponse> equipmentResponses = allByGymAndEquipment.stream()
                .map(EquipmentResponse::new).toList();

        return new EquipmentGymListResponse(gym.getName(), allByGymAndEquipment.size(), equipmentResponses);
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
