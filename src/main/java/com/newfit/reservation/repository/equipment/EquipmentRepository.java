package com.newfit.reservation.repository.equipment;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.Equipment;
import com.newfit.reservation.domain.equipment.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Gym, Name, Purpose가 모두 동일한 Equipment를 조회
    Optional<Equipment> findByGymAndNameAndPurpose(Gym gym, String name, Purpose purpose);
}
