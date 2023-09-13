package com.newfit.reservation.service.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
