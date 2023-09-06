package com.newfit.reservation.service;

import com.newfit.reservation.repository.EquipmentGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentGymService {

    private final EquipmentGymRepository equipmentGymRepository;
}
