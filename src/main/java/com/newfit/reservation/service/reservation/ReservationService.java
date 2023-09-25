package com.newfit.reservation.service.reservation;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.BusinessTime;
import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.domain.routine.EquipmentRoutine;
import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.dto.response.*;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import com.newfit.reservation.repository.routine.EquipmentRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final EquipmentRoutineRepository equipmentRoutineRepository;


    public ReservationResponse reserve(Long authorityId,
                                       Long equipmentId,
                                       ReservationRequest request) {

        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());

        Authority reserver = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);

        checkBusinessHour(request.getStartAt(), request.getEndAt(), reserver);

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = getOneAvailable(equipmentId, request.getStartAt(), request.getEndAt());

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

        // 예약 세트 횟수 변경
        if (request.getRepetitionNumber() != null) {
            targetReservation.updateRepetitionNumber(request.getRepetitionNumber());
        }

        // 예약 시간 변경
        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());
        checkBusinessHour(request.getStartAt(), request.getEndAt(), targetReservation.getReserver());

        // 같은 기구 예약 변경
        if (validateReservationOverlap(targetReservation.getEquipmentGym(), request.getStartAt(), request.getEndAt())) {
            targetReservation.updateTime(request);
        } else {
            // 다른 기구로 예약 변경
            Long targetEquipmentId = targetReservation.getEquipmentGym().getEquipment().getId();
            EquipmentGym anotherEquipment = getOneAvailable(targetEquipmentId, request.getStartAt(), request.getEndAt());

            Reservation updateReservation = Reservation.reserveAnother(targetReservation, anotherEquipment, request);
            reservationRepository.save(updateReservation);
            reservationRepository.delete(targetReservation);
        }
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
                .filter(reservation -> reservation.getEnd_at().isAfter(now) || reservation.getStart_at().isBefore(now.plusHours(2)))
                .map(reservation -> new OccupiedTime(reservation.getStart_at(), reservation.getEnd_at()))
                .collect(Collectors.toList());

        return new EquipmentInfoResponse(equipmentGym, occupiedTimes);
    }

    // 루틴을 예약
    public List<RoutineReservationResponse> reserveByRoutine(Long authorityId, Long routineId, LocalDateTime startAt) {
        List<RoutineReservationResponse> reservedList = new ArrayList<>();
        List<EquipmentRoutine> allInRoutine = equipmentRoutineRepository.findAllByRoutineIdOrderBySequence(routineId);

        for (EquipmentRoutine equipmentRoutine : allInRoutine) { //각 기구에 대해 예약 시도. 성공시 startAt에 duration 더하기.
            Long equipmentId = equipmentRoutine.getEquipment().getId();
            LocalDateTime endAt = startAt.plusMinutes(equipmentRoutine.getDuration().toMinutes());

            RoutineReservationResponse result = reserveOneInRoutine(authorityId, equipmentId, startAt, endAt);
            if (result.isSuccess()) {
                startAt = result.getStartAt().plusMinutes(equipmentRoutine.getDuration().toMinutes());
                reservedList.add(result);
            }
        }

        return reservedList;
    }

    // 루틴의 특정 기구를 예약
    private RoutineReservationResponse reserveOneInRoutine(Long authorityId, Long equipmentId, LocalDateTime startAt, LocalDateTime endAt) {
        Authority reserver = authorityRepository.findOne(authorityId)
                .orElseThrow(IllegalArgumentException::new);
        EquipmentGym equipmentGym = null;

        int attempt = 0;
        while (attempt != 5) {
            try {
                equipmentGym = getOneAvailable(equipmentId, startAt, endAt);
                Reservation reservation = Reservation.builder()
                        .reserver(reserver)
                        .equipmentGym(equipmentGym)
                        .startAt(startAt)
                        .endAt(endAt)
                        .repetitionNumber(0L)
                        .build();
                reservationRepository.save(reservation);
                return new RoutineReservationResponse(equipmentGym.getId(), true, startAt);
            } catch (NoSuchElementException exception) {
                startAt = startAt.plusMinutes(1);
                endAt = endAt.plusMinutes(1);
                attempt += 1;
            }
        }
        // 찾지 못한 경우
        return new RoutineReservationResponse(null, false, null);
    }

    private EquipmentGym getOneAvailable(Long equipmentId, LocalDateTime startAt, LocalDateTime endAt) {
        return equipmentGymRepository.findAvailableByEquipmentId(equipmentId)
                .stream()
                .filter(equipmentGym ->
                        validateReservationOverlap(equipmentGym, startAt, endAt))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("There is no available equipment"));
    }

    private void checkBusinessHour(LocalDateTime startAt, LocalDateTime endAt, Authority reserver) {

        /*
         예약의 시작 시간이 종료 시간보다 선행되는지 체크
         exception이 발생 안하면 예약 시작 시간이 종료 시간보다 선행됨을 보장
         */
        if (startAt.isAfter(endAt))
            throw new IllegalArgumentException("잘못된 예약 요청입니다.");

        Gym gym = reserver.getGym();
        BusinessTime businessTime = gym.getBusinessTime();

        // 헬스장이 24시간 운영이면 true 리턴
        if (businessTime.isAllDay())
            return;

        // 헬스장 오픈 시각(ex. 6 : 30)
        LocalTime gymOpenAt = businessTime.getOpenAt();
        // 헬스장 종료 시각(ex. 23 : 45)
        LocalTime gymCloseAt = businessTime.getCloseAt();

        // 헬스장 오픈 및 종료 시각에서 '시' 정보만 추출
        int gymOpenHour = gymOpenAt.getHour();
        int gymCloseHour = gymCloseAt.getHour();

        /*
        openHour < closeHour 이면 (openHour < 예약의 시작 시간) && (예약 종료 시간 < closeHour) 인지만 확인
        openHour > closeHour 이면 예약의 시작과 종료 시간 둘 다 헬스장 운영 시간 내에 속하는지 확인
         */
        boolean result = checkStartAtInBusinessHour(startAt, gymOpenHour, gymCloseHour, businessTime) &&
                checkEndAtInBusinessHour(endAt, gymOpenHour, gymCloseHour, businessTime);

        if (!result)
            throw new IllegalArgumentException("헬스장 운영 시간을 준수하지 않는 예약 요청입니다.");
    }

    private boolean checkEndAtInBusinessHour(LocalDateTime endAt, int gymOpenHour, int gymCloseHour,
                                             BusinessTime businessTime) {
        int reservationEndHour = endAt.getHour();
        int reservationEndMinute = endAt.getMinute();

        // 헬스장 종료 '시'와 예약의 종료 '시'가 같다면 '분' 비교
        if (reservationEndHour == gymCloseHour)
            return reservationEndMinute <= businessTime.getCloseAt().getMinute();
        // 헬스장 오픈 '시'와 예약의 종료 '시'가 같다면 '분' 비교
        if (reservationEndHour == gymOpenHour)
            return businessTime.getOpenAt().getMinute() < reservationEndMinute;

        /*
        openHour < closeHour (ex. 6AM ~ 23PM)이면 예약 종료 시간이 closeHour보다 앞서는지만 확인
        openHour > closeHour (ex. 4AM ~ 1AM)이면 예약 종료 시간이 헬스장 운영시간 안에 속하는지 확인
         */
        if (gymOpenHour < gymCloseHour)
            return reservationEndHour < gymCloseHour;
        else
            return (gymOpenHour < reservationEndHour) || (reservationEndHour < gymCloseHour);
    }

    private boolean checkStartAtInBusinessHour(LocalDateTime startAt, int gymOpenHour, int gymCloseHour,
                                               BusinessTime businessTime) {
        int reservationStartHour = startAt.getHour();
        int reservationStartMinute = startAt.getMinute();

        // 헬스장 오픈 '시'와 예약의 시작 '시'가 같다면 '분' 비교
        if (reservationStartHour == gymOpenHour)
            return businessTime.getOpenAt().getMinute() <= reservationStartMinute;
        // 헬스장 종료 '시'와 예약의 시작 '시'가 같다면 '분' 비교
        if (reservationStartHour == gymCloseHour)
            return reservationStartMinute < businessTime.getCloseAt().getMinute();

        /*
        openHour < closeHour (ex. 6AM ~ 23PM)이면 예약 시작 시간이 openHour보다 더 늦은지만 확인
        openHour > closeHour (ex. 4AM ~ 1AM)이면 예약 시작 시간이 헬스장 운영시간 안에 속하는지 확인
         */
        if (gymOpenHour < gymCloseHour)
            return gymOpenHour < reservationStartHour;
        else
            return (gymOpenHour < reservationStartHour) || (reservationStartHour < gymCloseHour);
    }

    private void validateReservationIn2Hours(LocalDateTime startAt, LocalDateTime endAt) {

        final long MAX_HOUR_TERM = 2L;
        final long MAX_MINUTE = 30L;

        LocalDateTime twoHourLater = LocalDateTime.now().plusHours(MAX_HOUR_TERM);

        if (startAt.isAfter(twoHourLater)) {
            throw new IllegalArgumentException("예약 시작 시간을 확인해주세요.");
        }

        if (endAt.isAfter(twoHourLater.plusMinutes(MAX_MINUTE))) {
            throw new IllegalArgumentException("예약 종료 시간을 확인해주세요.");
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
}
