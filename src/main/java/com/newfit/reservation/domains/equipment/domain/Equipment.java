package com.newfit.reservation.domains.equipment.domain;

import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.gym.domain.Gym;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Purpose purpose;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EquipmentGym> equipmentGyms = new ArrayList<>();

    @Builder
    public Equipment(Gym gym, String name, Purpose purpose) {
        this.gym = gym;
        this.name = name;
        this.purpose = purpose;
    }

    public static Equipment createEquipment(Gym gym, String name, Purpose purpose) {
        return Equipment.builder()
                .gym(gym)
                .name(name)
                .purpose(purpose)
                .build();
    }
}
