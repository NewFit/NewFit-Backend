package com.newfit.reservation.domains.equipment.repository;


import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.gym.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EquipmentGymRepository extends JpaRepository<EquipmentGym, Long> {
    List<EquipmentGym> findAllByGym(Gym gym);

    List<EquipmentGym> findAllByGymAndEquipment(Gym gym, Equipment equipment);

    @Query(value = "select eg.* from Equipment_Gym eg " +
            "join Equipment e on eg.equipment_id=e.id " +
            "where eg.gym_id=:gymId and e.purpose=:purpose", nativeQuery = true)
    List<EquipmentGym> findAllByGymAndPurpose(@Param("gymId") Long gymId, @Param("purpose") String purpose);

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
}
