package com.newfit.reservation.domain.equipment;

import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.Gym;
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
    private Condition condition;

    @Column(nullable = false)
    private String name;

    @Builder
    public EquipmentGym(Equipment equipment, Gym gym, String name) {
        this.equipment = equipment;
        this.gym = gym;
        this.condition = Condition.AVAILABLE;
        this.name = name;
    }

    public static EquipmentGym createEquipmentGym(Equipment equipment, Gym gym, String name) {
        return EquipmentGym.builder()
                .equipment(equipment)
                .gym(gym)
                .name(name)
                .build();
    }

    public void updateCondition(Condition condition) {
        this.condition = condition;
    }

    public void use() {
        this.condition = Condition.OCCUPIED;
    }

    public void restore(){
        this.condition = Condition.AVAILABLE;
    }
}
