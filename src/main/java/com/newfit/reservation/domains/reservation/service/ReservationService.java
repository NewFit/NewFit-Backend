package com.newfit.reservation.domains.reservation.service;


import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.authority.repository.AuthorityRepository;
import com.newfit.reservation.domains.credit.domain.Credit;
import com.newfit.reservation.domains.credit.repository.CreditRepository;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.dto.response.EquipmentInfoResponse;
import com.newfit.reservation.domains.equipment.dto.response.OccupiedTime;
import com.newfit.reservation.domains.equipment.repository.EquipmentGymRepository;
import com.newfit.reservation.domains.gym.domain.BusinessTime;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import com.newfit.reservation.domains.reservation.domain.Status;
import com.newfit.reservation.domains.reservation.dto.response.ReservationDetailResponse;
import com.newfit.reservation.domains.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.domains.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.domains.reservation.dto.request.ReservationUpdateRequest;
import com.newfit.reservation.domains.reservation.repository.ReservationRepository;
import com.newfit.reservation.domains.routine.domain.EquipmentRoutine;
import com.newfit.reservation.domains.routine.domain.Routine;
import com.newfit.reservation.domains.routine.dto.response.RoutineReservationListResponse;
import com.newfit.reservation.domains.routine.dto.response.RoutineReservationResponse;
import com.newfit.reservation.domains.routine.repository.EquipmentRoutineRepository;
import com.newfit.reservation.domains.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.*;
import static com.newfit.reservation.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AuthorityRepository authorityRepository;
    private final EquipmentGymRepository equipmentGymRepository;
    private final EquipmentRoutineRepository equipmentRoutineRepository;
    private final CreditRepository creditRepository;
    private final RoutineRepository routineRepository;

    public void reserve(Long authorityId,
                        Long equipmentId,
                        ReservationRequest request) {

        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        validateLastTagAt(authority);
        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());

        checkBusinessHour(request.getStartAt(), request.getEndAt(), authority);

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = getOneAvailable(equipmentId, request.getStartAt(), request.getEndAt());


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

        validateLastTagAt(targetReservation.getAuthority());

        // 예약 세트 횟수 변경
        if (request.getRepetitionNumber() != null) {
            targetReservation.updateRepetitionNumber(request.getRepetitionNumber());
        }

        // 예약 시간 변경
        validateReservationIn2Hours(request.getStartAt(), request.getEndAt());
        checkBusinessHour(request.getStartAt(), request.getEndAt(), targetReservation.getAuthority());

        if (request.getStartAt() != null)
            targetReservation.updateStartTime(request.getStartAt());

        if (request.getEndAt() != null)
            targetReservation.updateEndTime(request.getEndAt());


        // 다른 기구로 예약 변경
        if (!validateReservationOverlap(targetReservation.getEquipmentGym(), request.getStartAt(), request.getEndAt())) {
            Long targetEquipmentId = targetReservation.getEquipmentGym().getEquipment().getId();
            EquipmentGym anotherEquipmentGym =
                    getOneAvailable(targetEquipmentId, request.getStartAt(), request.getEndAt());
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

    // 루틴을 예약
    public RoutineReservationListResponse reserveByRoutine(Long authorityId, Long routineId, LocalDateTime startAt) {
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

        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new CustomException(ROUTINE_NOT_FOUND));
        routine.incrementCount();

        return RoutineReservationListResponse.create(reservedList);
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(RESERVATION_NOT_FOUND));
    }

    // 루틴의 특정 기구를 예약
    private RoutineReservationResponse reserveOneInRoutine(Long authorityId, Long equipmentId, LocalDateTime startAt, LocalDateTime endAt) {
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));
        EquipmentGym equipmentGym = null;

        int attempt = 0;
        while (attempt != 5) {
            try {
                equipmentGym = getOneAvailable(equipmentId, startAt, endAt);
                Reservation reservation = Reservation.create(authority, equipmentGym, startAt, endAt, 0L);
                reservationRepository.save(reservation);
                return new RoutineReservationResponse(equipmentGym.getId(), true, startAt);
            } catch (CustomException exception) { // getOneAvailable()에서 발생하는 예외
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
                .orElseThrow(() -> new CustomException(EQUIPMENT_GYM_NOT_FOUND, "예약 가능한 기구가 없습니다."));
    }

    private void checkBusinessHour(LocalDateTime startAt, LocalDateTime endAt, Authority authority) {

        /*
         예약의 시작 시간이 종료 시간보다 선행되는지 체크
         exception이 발생 안하면 예약 시작 시간이 종료 시간보다 선행됨을 보장
         */
        if (startAt.isAfter(endAt))
            throw new CustomException(INVALID_RESERVATION_REQUEST);

        Gym gym = authority.getGym();
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
            throw new CustomException(INVALID_RESERVATION_REQUEST, "요청이 헬스장 운영 시간과 맞지 않습니다.");
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

    public void checkConditionAndAddCredit(Reservation reservation, Authority authority, LocalDateTime endEquipmentUseAt) {
        LocalDateTime now = now();

        if (checkConditions(reservation, endEquipmentUseAt)) {
            if (authority.getCreditAcquisitionCount() != 10) {
                Credit credit = creditRepository.findByAuthorityAndYearAndMonth(authority, (short) now.getYear(), (short) now.getMonthValue())
                        .orElseGet(() -> creditRepository.save(Credit.createCredit(authority)));
                credit.addAmount();
                authority.incrementAcquisitionCount();
                authority.getUser().addBalance(100L);
            } else {
                throw new CustomException(MAXIMUM_CREDIT_LIMIT);
            }
        } else {
            throw new CustomException(INVALID_CREDIT_ACQUIRE_REQUEST, "크레딧 획득에 실패했습니다.");
        }
    }

    public void updateStatusAndCondition(Reservation reservation) {
        reservation.updateStatus(Status.COMPLETED);
        reservation.getEquipmentGym().restore();
    }

    private boolean checkConditions(Reservation reservation, LocalDateTime endTagAt) {
        return isStartTagInTime(reservation) && isEndTagInTime(reservation, endTagAt);
    }

    private boolean isStartTagInTime(Reservation reservation) {
        return (reservation.getStartTagAt().isBefore(reservation.getStartAt().plusMinutes(5)) && reservation.getStartTagAt().isAfter(reservation.getStartAt()))
                || reservation.getStartTagAt().isEqual(reservation.getStartAt().plusMinutes(5));
    }

    private boolean isEndTagInTime(Reservation reservation, LocalDateTime endTagAt) {
        return (endTagAt.isBefore(reservation.getEndAt().plusMinutes(5)) && endTagAt.isAfter(reservation.getEndAt()))
                || endTagAt.isEqual(reservation.getEndAt().plusMinutes(5));
    }

    private void validateReservationIn2Hours(LocalDateTime startAt, LocalDateTime endAt) {

        final long MAX_HOUR_TERM = 2L;
        final long MAX_MINUTE = 30L;

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
        // TODO: 시간은 기배님의 의견에 따라 결정
        if (tagAt.isBefore(now().minusMinutes(3))) {
            throw new CustomException(INVALID_TAG_REQUEST, "요청 시각이 현재 시간과 맞지 않습니다.");
        }

        if (tagAt.isBefore(reservationStartAt.minusMinutes(3))) {
            throw new CustomException(INVALID_TAG_REQUEST, "예약 시작 3분 전에 태그할 수 있습니다.");
        }
    }
}
