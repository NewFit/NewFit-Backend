package com.newfit.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SidoArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adm_code", nullable = false)
    private String admCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @OneToMany(mappedBy = "sidoArea")
    private List<SiggArea> siggAreas = new ArrayList<>();
}
