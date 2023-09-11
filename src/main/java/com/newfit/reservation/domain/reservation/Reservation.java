package com.newfit.reservation.domain.reservation;

import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserver_id", nullable = false)
    private User reserver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_gym_id", nullable = false)
    private EquipmentGym equipmentGym;

    @Column(nullable = false)
    private LocalDateTime start_at;

    @Column(nullable = false)
    private LocalDateTime end_at;

    @Column(nullable = false)
    private Long repetition_number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
