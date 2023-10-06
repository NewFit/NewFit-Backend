package com.newfit.reservation.repository.reservation;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.reserver.id = :authorityId")
    List<Reservation> findAllByAuthorityId(@Param("authorityId") Long authorityId);

    List<Reservation> findAllByEquipmentGym(EquipmentGym equipmentGym);

    Optional<Reservation> findByReserverAndEquipmentGym(Authority reserver, EquipmentGym equipmentGym);
}
