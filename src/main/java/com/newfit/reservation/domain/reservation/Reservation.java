package com.newfit.reservation.domain.reservation;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserver_id")
    private User reserver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_gym_id")
    private EquipmentGym equipmentGym;

    @Column(nullable = false)
    private LocalDateTime start_at;

    @Column(nullable = false)
    private LocalDateTime end_at;

    @Column(nullable = false)
    private Long repetition_number;

    @Column(nullable = false)
    private Status status;

}
