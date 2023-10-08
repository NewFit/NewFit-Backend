package com.newfit.reservation.domain.reservation;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.equipment.EquipmentGym;
import com.newfit.reservation.dto.request.ReservationRequest;
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
    private Authority authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_gym_id", nullable = false)
    private EquipmentGym equipmentGym;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private Long repetitionNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "start_tag_at")
    private LocalDateTime startTagAt;

    @Builder
    private Reservation(Authority authority,
                       EquipmentGym equipmentGym,
                       LocalDateTime startAt,
                       LocalDateTime endAt,
                       Long repetitionNumber) {
        this.authority = authority;
        this.equipmentGym = equipmentGym;
        this.startAt = startAt;
        this.endAt = endAt;
        this.repetitionNumber = repetitionNumber;
        this.status = Status.WAITING;
        this.startTagAt = null;
    }


    public void updateEquipmentGym(EquipmentGym equipmentGym) {
        this.equipmentGym = equipmentGym;
    }


    public void updateStartTime(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public void updateEndTime(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public void updateRepetitionNumber(Long repetitionNumber) {
        this.repetitionNumber = repetitionNumber;
    }

    public void updateStartTagAt(LocalDateTime startTagAt) {
        if (this.startTagAt != null) {
            throw new IllegalArgumentException("can't update twice");
        }
        this.startTagAt = startTagAt;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public boolean overlapped(LocalDateTime start, LocalDateTime end) {
        boolean startBeforeEnd = this.startAt.isBefore(this.endAt)
                && start.isBefore(end);

        boolean isBefore = this.endAt.isBefore(start);
        boolean isAfter = this.startAt.isAfter(end);

        return !(startBeforeEnd && (isBefore || isAfter));
    }


    public static Reservation create(Authority authority,
                                     EquipmentGym equipmentGym,
                                     ReservationRequest request) {
        return Reservation.builder()
                .authority(authority)
                .equipmentGym(equipmentGym)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .repetitionNumber(request.getRepetitionNumber())
                .build();
    }
}
