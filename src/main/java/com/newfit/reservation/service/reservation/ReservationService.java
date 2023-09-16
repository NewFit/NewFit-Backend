package com.newfit.reservation.service.reservation;


import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.request.ReservationRequest;
import com.newfit.reservation.dto.response.ReservationResponse;
import com.newfit.reservation.repository.AuthorityRepository;
import com.newfit.reservation.repository.UserRepository;
import com.newfit.reservation.repository.equipment.EquipmentGymRepository;
import com.newfit.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final EquipmentGymRepository equipmentGymRepository;


    private void validateReserverRegistration(Long userId, Gym gym) {

        authorityRepository.findAuthoritiesByUserId(userId)
                .stream().parallel()
                .filter(a -> a.getGym()
                        .equals(gym))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public ReservationResponse reserve(Long userId,
                                       Long equipmentId,
                                       ReservationRequest request) {

        User reserver = userRepository.findOne(userId)
                .orElseThrow(IllegalArgumentException::new);

        // 사용 가능한 기구 하나를 가져옴
        EquipmentGym usedEquipment = equipmentGymRepository.findAvailableByEquipmentId(equipmentId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException("There is no available equipment"));


        // 사용자가 헬스장에 등록되어 있는지 확인
        validateReserverRegistration(userId, usedEquipment.getGym());


        usedEquipment.use();
        equipmentGymRepository.save(usedEquipment);

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
}
