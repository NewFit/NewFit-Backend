package com.newfit.reservation.domains.equipment.domain;

import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.gym.domain.Gym;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EquipmentGym extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    @Column(nullable = false)
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    private EquipmentGym(Equipment equipment, Gym gym, String name) {
        this.equipment = equipment;
        this.gym = gym;
        this.conditionType = ConditionType.AVAILABLE;
        this.name = name;
    }

    public static EquipmentGym createEquipmentGym(Equipment equipment, Gym gym, String name) {
        return EquipmentGym.builder()
                .equipment(equipment)
                .gym(gym)
                .name(name)
                .build();
    }

    public void updateCondition(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public void use() {
        this.conditionType = ConditionType.OCCUPIED;
    }

    public void restore() {
        this.conditionType = ConditionType.AVAILABLE;
    }
}
