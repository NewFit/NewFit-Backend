package com.newfit.reservation.service.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.equipment.Purpose;
import com.newfit.reservation.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.dto.response.EquipmentResponse;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentGymService {

    private final EquipmentGymRepository equipmentGymRepository;

    /*
    입력받은 개수(count)만큼 등록
     */
    public void registerEquipmentInGym(Equipment equipment, Gym gym, Integer count) {
        IntStream.range(0, count)
                .forEach(repeat -> equipmentGymRepository.save(EquipmentGym.createEquipmentGym(equipment, gym)));
    }

    /*
    Gym으로 EquipmentGym을 모두 조회
    Response를 위한 Dto에 담아 반환
     */
    public EquipmentGymListResponse findAllInGym(Gym gym) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);

        List<EquipmentResponse> equipmentResponses = allByGym.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());

        EquipmentGymListResponse response = new EquipmentGymListResponse(gym.getName(), allByGym.size(), equipmentResponses);
        return response;
    }

    /*
    EquipmentGym의 Condition을 수정
     */
    public void updateCondition(Long equipmentGymId, Condition condition) {
        EquipmentGym equipmentGym = equipmentGymRepository.findById(equipmentGymId).get();
        equipmentGym.updateCondition(condition);
    }

    /*
    equipmentGym 삭제
     */
    public void deleteEquipmentGym(Long equipmentGymId) {
        EquipmentGym equipmentGym = equipmentGymRepository.findById(equipmentGymId).get();
        equipmentGymRepository.delete(equipmentGym);
    }

    /*
    gym과 purpose로 EquipmentGymList 조회
     */
    public EquipmentGymListResponse findAllInGymByPurpose(Gym gym, Purpose purpose) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);
        List<EquipmentGym> allByGymAndPurpose = allByGym
                .stream()
                .filter(equipmentGym -> equipmentGym.getEquipment().getPurpose().equals(purpose))
                .collect(Collectors.toList());

        List<EquipmentResponse> equipmentResponses = allByGymAndPurpose.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        EquipmentGymListResponse response = new EquipmentGymListResponse(gym.getName(), allByGymAndPurpose.size(), equipmentResponses);
        return response;
    }

    /*
    gym과 equipment로 EquipmentGymList 조회
     */
    public EquipmentGymListResponse findAllInGymByEquipment(Gym gym, Equipment equipment) {
        List<EquipmentGym> allByGym = equipmentGymRepository.findAllByGym(gym);
        List<EquipmentGym> allByGymAndEquipment = allByGym
                .stream()
                .filter(equipmentGym -> equipmentGym.getEquipment().equals(equipment))
                .collect(Collectors.toList());

        List<EquipmentResponse> equipmentResponses = allByGymAndEquipment.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());

        EquipmentGymListResponse response = new EquipmentGymListResponse(gym.getName(), allByGymAndEquipment.size(), equipmentResponses);
        return response;
    }

    /*
    EquipmentGymId로 EquipmentGym 조회
     */
    public EquipmentGym findOneById(Long equipmentGymId) {
        return equipmentGymRepository.findById(equipmentGymId).get();
    }
}
