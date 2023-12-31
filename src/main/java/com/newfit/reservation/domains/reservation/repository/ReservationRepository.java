package com.newfit.reservation.domains.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

	List<Reservation> findAllByEquipmentGym(EquipmentGym equipmentGym);

	List<Reservation> findAllByStartAt(LocalDateTime startAt);

	Optional<Reservation> findByAuthorityAndEquipmentGym(Authority authority, EquipmentGym equipmentGym);

	@Query(value = "select r.* from Reservation r " +
		"join authority a " +
		"on a.id=:authorityId " +
		"where ( " +
		"(:startAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:endAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:startAt <= r.start_at AND r.end_at <= :endAt));", nativeQuery = true)
	List<Reservation> findAllByAuthorityIdAndStartAtAndEndAt(@Param("authorityId") Long authorityId,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt);

	@Query(value = "select r.* from Reservation r " +
		"join equipment_gym eg " +
		"on eg.id=:equipmentGymId " +
		"where ( " +
		"(:startAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:endAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:startAt <= r.start_at AND r.end_at <= :endAt));", nativeQuery = true)
	List<Reservation> findAllByEquipmentGymIdIdAndStartAtAndEndAt(@Param("equipmentGymId") Long equipmentGymId,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt);

	@Query(value = "select r.* from Reservation r " +
		"join authority a " +
		"on a.id=:authorityId " +
		"where ( " +
		"(:startAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:endAt BETWEEN r.start_at AND r.end_at) " +
		"OR (:startAt <= r.start_at AND r.end_at <= :endAt)) " +
		"AND (r.id <> :reservationId);", nativeQuery = true)
	List<Reservation> findAllByAuthorityIdAndStartAtAndEndAtExcludingCurrent(@Param("authorityId") Long authorityId,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt,
		@Param("reservationId") Long reservationId);
}
