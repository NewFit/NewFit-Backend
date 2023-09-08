package com.newfit.reservation.service.routine;

import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentRoutineService {

    private final EquipmentRoutineRepository equipmentRoutineRepository;
}
