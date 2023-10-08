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

    @Column(nullable = false)
    private Short sequence;

    @Builder(access = AccessLevel.PRIVATE)
    private EquipmentRoutine(Equipment equipment, Routine routine, Duration duration, Short sequence) {
        this.equipment = equipment;
        this.routine = routine;
        this.duration = duration;
        this.sequence = sequence;
    }

    public static EquipmentRoutine createEquipmentRoutine(Equipment equipment, Routine routine,
                                                  Duration duration, Short sequence) {
        return EquipmentRoutine.builder()
                .equipment(equipment)
                .routine(routine)
                .duration(duration)
                .sequence(sequence)
                .build();
    }

    public void updateSequence(Short sequence) {
        this.sequence = sequence;
    }

    public void updateDuration(Duration duration) {
        this.duration = duration;
    }
}
