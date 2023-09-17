package com.newfit.reservation.domain.reservation;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.dto.request.ReservationUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority reserver;

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


    @Builder
    public Reservation(Authority reserver,
                       EquipmentGym equipmentGym,
                       LocalDateTime startAt,
                       LocalDateTime endAt,
                       Long repetitionNumber) {
        this.reserver = reserver;
        this.equipmentGym = equipmentGym;
        this.start_at = startAt;
        this.end_at = endAt;
        this.repetition_number = repetitionNumber;
        this.status = Status.WAITING;
    }


    public void update(ReservationUpdateRequest request){
        this.start_at = request.getStartAt();
        this.end_at = request.getEndAt();
    }
}
