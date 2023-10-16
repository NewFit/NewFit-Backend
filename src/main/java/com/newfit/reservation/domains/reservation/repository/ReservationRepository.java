package com.newfit.reservation.domains.reservation.repository;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.authority.id = :authorityId")
    List<Reservation> findAllByAuthorityId(@Param("authorityId") Long authorityId);

    List<Reservation> findAllByEquipmentGym(EquipmentGym equipmentGym);

    Optional<Reservation> findByAuthorityAndEquipmentGym(Authority authority, EquipmentGym equipmentGym);
}
