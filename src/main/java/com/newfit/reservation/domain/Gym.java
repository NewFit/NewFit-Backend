package com.newfit.reservation.domain;

import com.newfit.reservation.domain.location.EmdArea;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emd_id")
    private EmdArea location;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tel;
}
