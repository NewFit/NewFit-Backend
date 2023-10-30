package com.newfit.reservation.domains.equipment.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.equipment.repository.EquipmentRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.routine.domain.EquipmentRoutine;
import com.newfit.reservation.domains.routine.repository.EquipmentRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.newfit.reservation.common.exception.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentGymRepository equipmentGymRepository;
    private final EquipmentRoutineRepository equipmentRoutineRepository;

    /*
    Equipment를 새로 등록.
    이미 존재한다면 등록없이 반환.
     */
    public Equipment registerEquipment(Gym gym, String name, PurposeType purposeType) {
        checkExistingEquipment(gym, name, purposeType);
        return equipmentRepository.save(Equipment.createEquipment(gym, name, purposeType));
    }

    private void checkExistingEquipment(Gym gym, String name, PurposeType purposeType) {
        if (equipmentRepository.findByGymAndNameAndPurposeType(gym, name, purposeType).isPresent()) {
            throw new CustomException(ALREADY_EXISTING_EQUIPMENT);
        }
    }

    /*
    equipment 삭제
     */
    public void deleteEquipment(Long equipmentId) {
        Equipment equipment = findById(equipmentId);
        equipment.deactivate();

        List<EquipmentGym> equipmentGyms = equipmentGymRepository.findAllByEquipment_Id(equipmentId);
        equipmentGyms.forEach(EquipmentGym::deactivate);

        List<EquipmentRoutine> equipmentRoutines = equipmentRoutineRepository.findAllByEquipment_Id(equipmentId);
        equipmentRoutines.forEach(EquipmentRoutine::deactivate);
    }

    /*
    EquipmentId로 Equipment조회
     */
    public Equipment findById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new CustomException(EQUIPMENT_NOT_FOUND));
    }
}
