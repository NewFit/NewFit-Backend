package com.newfit.reservation.repository;


import com.newfit.reservation.domain.equipment.EquipmentGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long> {
}
