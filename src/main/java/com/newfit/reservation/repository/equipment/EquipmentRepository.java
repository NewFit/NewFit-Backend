package com.newfit.reservation.repository.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Gym, Name, Purpose가 모두 동일한 Equipment를 조회
    Optional<Equipment> findByGymAndNameAndPurpose(Gym gym, String name, Purpose purpose);

    // Equipment의 id와 Gym 객체로 Equipment 객체를 조회합니다.
    Optional<Equipment> findByIdAndGym(Long equipmentId, Gym gym);
}
