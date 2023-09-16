package com.newfit.reservation.domain.routine;


import com.newfit.reservation.domain.equipment.Equipment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EquipmentRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Column(nullable = false)
    private Duration duration;

    @Builder
    private EquipmentRoutine(Equipment equipment, Routine routine, Duration duration) {
        this.equipment = equipment;
        this.routine = routine;
        this.duration = duration;
    }
}
