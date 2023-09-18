package com.newfit.reservation.service.reservation;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.response.*;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AuthorityRepository authorityRepository;
    private final EquipmentGymRepository equipmentGymRepository;

    /**
     * 대상 기구의 예약 현황에 요청 시간이 중복되면 false 를 반환함
     *
     * @param equipmentGym 대상 특정 기구
     * @param start        시작 시간
     * @param end          종료 시간
     */
    private boolean validateReservationOverlap(EquipmentGym equipmentGym, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findAllByEquipmentGym(equipmentGym)
                .stream()
                .filter(reservation ->
                        reservation.overlapped(start, end)
                )
                .findAny()
                .isEmpty();
    }

    public ReservationResponse reserve(Long authorityId,
                                       Long equipmentId,
                                       ReservationRequest request) {

        Authority reserver = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = equipmentGymRepository.findAvailableByEquipmentId(equipmentId)
                .stream()
                .filter(equipmentGym ->
                        validateReservationOverlap(equipmentGym, request.getStartAt(), request.getEndAt()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("There is no available equipment"));


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
    public ReservationListResponse listReservation(Long equipmentGymId) {
        EquipmentGym equipmentGym = equipmentGymRepository.findById(equipmentGymId)
                .orElseThrow(IllegalArgumentException::new);

        String gymName = equipmentGym.getGym().getName();

        List<ReservationDetailResponse> reservationDetailResponseList =
                reservationRepository.findAllByEquipmentGym(equipmentGym)
                        .stream()
                        .map(ReservationDetailResponse::new)
                        .toList();

        return ReservationListResponse.builder()
                .gymName(gymName)
                .reservationResponseList(reservationDetailResponseList)
                .build();
    }

    public ReservationResponse update(Long reservationId, ReservationUpdateRequest request) {
        Reservation targetReservation = reservationRepository.findById(reservationId)
                .orElseThrow(IllegalArgumentException::new);


        if (!validateReservationOverlap(targetReservation.getEquipmentGym(), request.getStartAt(), request.getEndAt())) {
            throw new IllegalArgumentException("Request is overlapped");
        }

        targetReservation.update(request);

        return new ReservationResponse(reservationId);
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public EquipmentInfoResponse getAllOccupiedTimes(EquipmentGym equipmentGym) {
        final LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservations = reservationRepository.findAllByEquipmentGym(equipmentGym);

        // reservations에서 현재 시간보다 늦게 끝나거나 현재 시간으로부터 2시간 이후내로 시작되는 예약을 OccupiedTime 리스트로 변환
        List<OccupiedTime> occupiedTimes = reservations.stream()
                .filter(reservation -> reservation.getEnd_at().isAfter(now) || reservation.getStart_at().isAfter(now.plusHours(2)))
                .map(reservation -> new OccupiedTime(reservation.getStart_at(), reservation.getEnd_at()))
                .collect(Collectors.toList());

        return new EquipmentInfoResponse(equipmentGym, occupiedTimes);
    }
}
