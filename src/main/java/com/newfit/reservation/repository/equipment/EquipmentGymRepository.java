package com.newfit.reservation.repository.equipment;


import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long> {
    List<EquipmentGym> findAllByGym(Gym gym);
}
