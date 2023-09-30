package com.newfit.reservation.domain.reservation;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.dto.request.ReservationRequest;
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

    @Column(name = "start_tag_at")
    private LocalDateTime startTagAt;

    @Builder
    private Reservation(Authority reserver,
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
        this.startTagAt = null;
    }


    public void updateEquipmentGym(EquipmentGym equipmentGym) {
        this.equipmentGym = equipmentGym;
    }


    public void updateStartTime(LocalDateTime startAt) {
        this.start_at = startAt;
    }

    public void updateEndTime(LocalDateTime endAt) {
        this.end_at = endAt;
    }

    public void updateRepetitionNumber(Long repetitionNumber) {
        this.repetition_number = repetitionNumber;
    }

    public boolean overlapped(LocalDateTime start, LocalDateTime end) {
        boolean startBeforeEnd = this.start_at.isBefore(this.end_at)
                && start.isBefore(end);

        boolean isBefore = this.end_at.isBefore(start);
        boolean isAfter = this.start_at.isAfter(end);

        return !(startBeforeEnd && (isBefore || isAfter));
    }


    public static Reservation create(Authority reserver,
                                     EquipmentGym equipmentGym,
                                     ReservationRequest request) {
        return Reservation.builder()
                .reserver(reserver)
                .equipmentGym(equipmentGym)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .repetitionNumber(request.getRepetitionNumber())
                .build();
    }
}
