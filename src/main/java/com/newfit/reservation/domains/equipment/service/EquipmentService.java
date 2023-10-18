package com.newfit.reservation.domains.equipment.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.Purpose;
import com.newfit.reservation.domains.equipment.repository.EquipmentRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.newfit.reservation.common.exception.ErrorCode.*;

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
        checkExistingEquipment(gym, name, purpose);
        return equipmentRepository.save(Equipment.createEquipment(gym, name, purpose));
    }

    private void checkExistingEquipment(Gym gym, String name, Purpose purpose) {
        if (equipmentRepository.findByGymAndNameAndPurpose(gym, name, purpose).isPresent()) {
            throw new CustomException(ALREADY_EXISTING_EQUIPMENT);
        }
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
                .orElseThrow(() -> new CustomException(EQUIPMENT_NOT_FOUND));
    }
}
