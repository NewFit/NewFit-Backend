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
