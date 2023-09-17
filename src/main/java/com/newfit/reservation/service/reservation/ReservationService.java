package com.newfit.reservation.service.reservation;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.response.ReservationDetailResponse;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.dto.response.ReservationResponse;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AuthorityRepository authorityRepository;
    private final EquipmentGymRepository equipmentGymRepository;


    public ReservationResponse reserve(Long authorityId,
                                       Long equipmentId,
                                       ReservationRequest request) {

        Authority reserver = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = equipmentGymRepository.findAvailableByEquipmentId(equipmentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no available equipment"));

        usedEquipment.use();

        Reservation reservation = Reservation.builder()
                .reserver(reserver)
                .equipmentGym(usedEquipment)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .repetitionNumber(request.getRepetitionNumber())
                .build();

        Reservation result = reservationRepository.save(reservation);

        return new ReservationResponse(result.getId());
    }

    @Transactional(readOnly = true)
    public ReservationListResponse listReservation(Long authorityId) {
        String gymName = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new)
                .getGym()
                .getName();

        return ReservationListResponse.builder()
                .gymName(gymName)
                .reservationResponseList(reservationRepository.findAllByAuthorityId(authorityId)
                        .stream()
                        .map(ReservationDetailResponse::new)
                        .collect(Collectors.toList()))
                .build();
    }

    public ReservationResponse update(Long reservationId, ReservationUpdateRequest request) {
        reservationRepository.findById(reservationId)
                .orElseThrow()
                .update(request);

        return new ReservationResponse(reservationId);
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
