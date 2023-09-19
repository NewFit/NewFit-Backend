package com.newfit.reservation.repository.equipment;


import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long> {
    List<EquipmentGym> findAllByGym(Gym gym);

    @Query("select e from EquipmentGym e where e.equipment.id = :equipmentId and e.condition='AVAILABLE'")
    List<EquipmentGym> findAvailableByEquipmentId(@Param("equipmentId") Long equipmentId);
}
