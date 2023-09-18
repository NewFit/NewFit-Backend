package com.newfit.reservation.domain.routine;


import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.User;
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

    @Builder
    private Routine(Authority authority, String name) {
        this.authority = authority;
        this.name = name;
    }
}
