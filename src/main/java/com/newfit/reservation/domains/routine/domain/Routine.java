package com.newfit.reservation.domains.routine.domain;

import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.authority.domain.Authority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @Column(nullable = false)
    private String name;

    private Long count = 0L;

    public void updateName(String name) {
        this.name = name;
    }

    public void incrementCount() {
        this.count++;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Routine(Authority authority, String name) {
        this.authority = authority;
        this.name = name;
    }

    public static Routine createRoutine(Authority authority, String name) {
        return Routine.builder()
                .authority(authority)
                .name(name)
                .build();
    }
}
