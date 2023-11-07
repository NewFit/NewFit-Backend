package com.newfit.reservation.domains.reservation.domain;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static com.newfit.reservation.common.exception.ErrorCodeType.ALREADY_TAGGED_RESERVATION;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_gym_id", nullable = false)
    private EquipmentGym equipmentGym;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(name = "start_tag_at")
    private LocalDateTime startTagAt;


    @Builder(access = AccessLevel.PRIVATE)
    private Reservation(Authority authority,
                        EquipmentGym equipmentGym,
                        LocalDateTime startAt,
                        LocalDateTime endAt) {
        this.authority = authority;
        this.equipmentGym = equipmentGym;
        this.startAt = startAt;
        this.endAt = endAt;
        this.statusType = StatusType.WAITING;
        this.startTagAt = null;
    }

    public void removeAuthority() {
        this.authority = null;
    }

    public void updateEquipmentGym(EquipmentGym equipmentGym) {
        this.equipmentGym = equipmentGym;
    }

    public void updateTime(LocalDateTime startAt, LocalDateTime endAt) {
        updateStartTime(startAt);
        updateEndTime(endAt);

    }

    private void updateStartTime(LocalDateTime startAt) {
        if (startAt != null) {
            this.startAt = startAt;
        }
    }

    private void updateEndTime(LocalDateTime endAt) {
        if (endAt != null) {
            this.endAt = endAt;
        }
    }

    public void updateStartTagAt(LocalDateTime startTagAt) {
        if (this.startTagAt != null) {
            throw new CustomException(ALREADY_TAGGED_RESERVATION);
        }
        this.startTagAt = startTagAt;
    }

    public void updateStatus(StatusType statusType) {
        this.statusType = statusType;
    }

    public static Reservation create(Authority authority,
                                     EquipmentGym equipmentGym,
                                     LocalDateTime startAt,
                                     LocalDateTime endAt) {
        return Reservation.builder()
                .authority(authority)
                .equipmentGym(equipmentGym)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
