package com.newfit.reservation.service.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Condition;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
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
                .map(e -> new EquipmentResponse(e.getId(), e.getEquipment().getName(), e.getEquipment().getPurpose(), e.getCondition()))
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
}
