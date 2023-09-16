package com.newfit.reservation.service.reservation;


import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.dto.response.OccupiedTime;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public EquipmentInfoResponse getAllOccupiedTimes(EquipmentGym equipmentGym) {
        final LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservations = reservationRepository.findAllByEquipmentGym(equipmentGym);

        // reservations에서 현재 시간보다 늦게 끝나거나 현재 시간으로부터 3시간 이후내로 시작되는 예약을 OccupiedTime 리스트로 변환
        List<OccupiedTime> occupiedTimes = reservations.stream()
                .filter(reservation -> reservation.getEnd_at().isAfter(now) || reservation.getStart_at().isAfter(now.plusHours(3)))
                .map(reservation -> new OccupiedTime(reservation.getStart_at(), reservation.getEnd_at()))
                .collect(Collectors.toList());

        return new EquipmentInfoResponse(equipmentGym, occupiedTimes);
    }
}
