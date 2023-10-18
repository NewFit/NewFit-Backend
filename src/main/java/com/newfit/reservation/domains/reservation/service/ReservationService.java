package com.newfit.reservation.domains.reservation.service;


import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.domains.equipment.dto.response.OccupiedTime;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.domain.Status;
import com.newfit.reservation.domains.reservation.dto.response.ReservationDetailResponse;
import com.newfit.reservation.domains.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.domains.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.domains.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.domains.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.*;
import static com.newfit.reservation.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final long MAX_HOUR_TERM = 2L;
    private final long MAX_MINUTE = 30L;

    private final ReservationRepository reservationRepository;
    private final AuthorityRepository authorityRepository;
    private final EquipmentGymRepository equipmentGymRepository;

    public void reserve(Long authorityId,
                        Long equipmentId,
                        ReservationRequest request) {

        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        validateLastTagAt(authority);
        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());

        Gym gym = authority.getGym();
        gym.checkBusinessHour(request.getStartAt(), request.getEndAt());

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = equipmentGymRepository.findAvailableByEquipmentIdAndStartAtAndEndAt(equipmentId, request.getStartAt(), request.getEndAt())
                .orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));
        Reservation reservation = Reservation.create(authority, usedEquipment, request.getStartAt(), request.getEndAt(), request.getRepetitionNumber());

        reservationRepository.save(reservation);
    }

    private void validateLastTagAt(Authority authority) {
        LocalDateTime tagAt = authority.getTagAt();
        if (tagAt.isBefore(now().minusHours(2)))
            throw new CustomException(EXPIRED_TAG);
    }

    @Transactional(readOnly = true)
    public ReservationListResponse listReservation(Long equipmentGymId) {
        EquipmentGym equipmentGym = equipmentGymRepository.findById(equipmentGymId)
                .orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));

        String gymName = equipmentGym.getGym().getName();

        List<ReservationDetailResponse> reservationDetailResponseList =
                reservationRepository.findAllByEquipmentGym(equipmentGym)
                        .stream()
                        .map(ReservationDetailResponse::new).toList();

        return ReservationListResponse.createResponse(gymName, reservationDetailResponseList);
    }

    public void update(Long reservationId, ReservationUpdateRequest request) {
        Reservation targetReservation = findById(reservationId);
        Authority authority = targetReservation.getAuthority();

        validateLastTagAt(authority);

        // 예약 세트 횟수 변경
        targetReservation.updateRepetitionNumber(request.getRepetitionNumber());

        // 예약 시간 변경
        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());
        checkBusinessHour(request.getStartAt(), request.getEndAt(), targetReservation.getAuthority());
      
        targetReservation.updateStartTime(request.getStartAt());
        targetReservation.updateEndTime(request.getEndAt());


        // 다른 기구로 예약 변경
        if (!validateReservationOverlap(targetReservation.getEquipmentGym(), request.getStartAt(), request.getEndAt())) {
            Long targetEquipmentId = targetReservation.getEquipmentGym().getEquipment().getId();
            EquipmentGym anotherEquipmentGym =
                    equipmentGymRepository.findAvailableByEquipmentIdAndStartAtAndEndAt(targetEquipmentId, request.getStartAt(), request.getEndAt())
                            .orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));
            targetReservation.updateEquipmentGym(anotherEquipmentGym);
        }
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public EquipmentInfoResponse getAllOccupiedTimes(EquipmentGym equipmentGym) {
        final LocalDateTime now = now();

        List<Reservation> reservations = reservationRepository.findAllByEquipmentGym(equipmentGym);

        // reservations에서 현재 시간보다 늦게 끝나거나 현재 시간으로부터 2시간 이후내로 시작되는 예약을 OccupiedTime 리스트로 변환
        List<OccupiedTime> occupiedTimes = reservations.stream()
                .filter(reservation -> reservation.getEndAt().isAfter(now) || reservation.getStartAt().isBefore(now.plusHours(2)))
                .map(reservation -> new OccupiedTime(reservation.getStartAt(), reservation.getEndAt())).toList();

        return new EquipmentInfoResponse(equipmentGym, occupiedTimes);
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(RESERVATION_NOT_FOUND));
    }

    public void updateStatusAndCondition(Reservation reservation) {
        reservation.updateStatus(Status.COMPLETED);
        reservation.getEquipmentGym().restore();
    }

    private void validateReservationIn2Hours(LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime twoHourLater = now().plusHours(MAX_HOUR_TERM);

        if (startAt.isAfter(twoHourLater)) {
            throw new CustomException(INVALID_RESERVATION_REQUEST, "예약 시작 시간을 확인해주세요.");
        }

        if (endAt.isAfter(twoHourLater.plusMinutes(MAX_MINUTE))) {
            throw new CustomException(INVALID_RESERVATION_REQUEST, "예약 종료 시간을 확인해주세요.");
        }
    }

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

    public void startUse(Long authorityId, Long equipmentGymId, LocalDateTime tagAt) {

        Authority authority = authorityRepository.findById(authorityId).orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));
        EquipmentGym equipmentGym = equipmentGymRepository.findById(equipmentGymId).orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND));
        Reservation reservation = reservationRepository.findByAuthorityAndEquipmentGym(authority, equipmentGym).orElseThrow(() -> new CustomException(RESERVATION_NOT_FOUND));

        validateTagAt(tagAt, reservation.getStartAt()); // 요청 시각과 현재 시각을 비교

        updateReservation(reservation, tagAt);
        equipmentGym.use();
        authority.updateTagAt(tagAt);
    }

    private void updateReservation(Reservation reservation, LocalDateTime tagAt) {
        reservation.updateStartTagAt(tagAt);
        reservation.updateStatus(Status.PROCESSING);
    }


    private void validateTagAt(LocalDateTime tagAt, LocalDateTime reservationStartAt) {
        if (tagAt.isBefore(now().minusMinutes(5))) {
            throw new CustomException(INVALID_TAG_REQUEST, "요청 시각이 현재 시간과 맞지 않습니다.");
        }

        if (tagAt.isBefore(reservationStartAt.minusMinutes(5))) {
            throw new CustomException(INVALID_TAG_REQUEST, "예약 시작 5분 전부터 태그할 수 있습니다.");
        }
    }
}
