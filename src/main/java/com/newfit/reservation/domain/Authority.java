package com.newfit.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Entity 클래스는 기본생성자가 필수로 있어야 합니다.
                                                    // 다만, Entity 객체를 직접 생성자로 생성할 일은 없을듯 하여 protected 로 설정했습니다.
public class Authority {

    // Authority 테이블 PK 입니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 테이블과의 연관관계를 나타내는 FK 입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Gym 테이블과의 연관관계를 나타내는 FK 입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    // 사용자의 역할(권한)을 나타냅니다.
    // Role 이라는 Enum 을 새로 정의했습니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 해당 사용자의 승인 여부를 나타냅니다.
    @Column(nullable = false)
    private Boolean accepted;

    // 양방향 연관관계를 나타냅니다.
    @OneToMany(mappedBy = "authority")
    private List<Credit> creditList = new ArrayList<>();
}
