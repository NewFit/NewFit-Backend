package com.newfit.reservation.service.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.Purpose;
import com.newfit.reservation.repository.equipment.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    /*
    Equipment를 새로 등록.
    이미 존재한다면 등록없이 반환.
     */
    public Equipment registerEquipment(Gym gym, String name, Purpose purpose) {
        if (doesExists(gym, name, purpose)) {
            return equipmentRepository.findByGymAndNameAndPurpose(gym, name, purpose).get();
        }
        return equipmentRepository.save(Equipment.createEquipment(gym, name, purpose));
    }

    /*
    registerEquipment 메서드에서 이미 존재하는 메서드인지 확인하기 위해 호출
     */
    public boolean doesExists(Gym gym, String name, Purpose purpose) {
        // pk를 제외한 모든 것이 일치하는지 boolean으로 반환
        return equipmentRepository.findByGymAndNameAndPurpose(gym, name, purpose).isPresent();
    }

    /*
    equipment 삭제
     */
    public void deleteEquipment(Long equipmentId) {
        equipmentRepository.deleteById(equipmentId);
    }

    /*
    EquipmentId로 Equipment조회
     */
    public Equipment findById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
