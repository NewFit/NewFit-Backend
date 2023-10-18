package com.newfit.reservation.domains.equipment.repository;

import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.Purpose;
import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Gym, Name, Purpose가 모두 동일한 Equipment를 조회
    Optional<Equipment> findByGymAndNameAndPurpose(Gym gym, String name, Purpose purpose);
}
