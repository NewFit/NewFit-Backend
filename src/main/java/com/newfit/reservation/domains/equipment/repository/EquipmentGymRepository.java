package com.newfit.reservation.domains.equipment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.gym.domain.Gym;

public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long>, EquipmentGymRepositoryCustom {
	List<EquipmentGym> findAllByGym(Gym gym);

	List<EquipmentGym> findAllByGymAndEquipment(Gym gym, Equipment equipment);

	List<EquipmentGym> findAllByEquipment_Id(Long equipmentId);

	@Query(value = "SELECT * " +
		"FROM Equipment_Gym eg " +
		"WHERE eg.equipment_id = :equipmentId" +
		"  AND NOT EXISTS (" +
		"    SELECT 1 FROM Reservation r" +
		"    WHERE r.equipment_gym_id = eg.id AND " +
		"        ((:startAt BETWEEN r.start_at AND r.end_at)" +
		"        OR (:endAt BETWEEN r.start_at AND r.end_at)" +
		"        OR (:startAt <= r.start_at AND r.end_at <= :endAt))) " +
		"LIMIT 1;", nativeQuery = true)
	Optional<EquipmentGym> findAvailableByEquipmentIdAndStartAtAndEndAt(@Param("equipmentId") Long equipmentId,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt);

	@Query(value = "SELECT * " +
		"FROM Equipment_Gym eg " +
		"WHERE eg.id = :equipmentGymId" +
		"  AND NOT EXISTS (" +
		"    SELECT 1 FROM Reservation r" +
		"    WHERE r.equipment_gym_id = eg.id AND " +
		"        ((:startAt BETWEEN r.start_at AND r.end_at)" +
		"        OR (:endAt BETWEEN r.start_at AND r.end_at)" +
		"        OR (:startAt <= r.start_at AND r.end_at <= :endAt))) " +
		"LIMIT 1;", nativeQuery = true)
	Optional<EquipmentGym> findAvailableByEquipmentGymIdAndStartAtAndEndAt(@Param("equipmentGymId") Long equipmentGymId,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt);
}
