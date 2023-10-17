package com.newfit.reservation.domains.equipment.repository;


import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long> {
    List<EquipmentGym> findAllByGym(Gym gym);

    @Query("select e from EquipmentGym e where e.equipment.id = :equipmentId")
    List<EquipmentGym> findAvailableByEquipmentId(@Param("equipmentId") Long equipmentId);
}
