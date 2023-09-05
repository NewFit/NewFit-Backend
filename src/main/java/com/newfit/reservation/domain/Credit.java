package com.newfit.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Entity 클래스는 기본생성자가 필수로 있어야 합니다.
                                                    // 다만, Entity 객체를 직접 생성자로 생성할 일은 없을듯 하여 protected 로 설정했습니다.
public class Credit {

    // Credit 테이블 PK 입니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Authority 테이블과의 연관관계를 나타내는 FK 입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;
    
    // 사용자가 크레딧을 어느 연도에 획득했는지 나타냅니다.
    // ERD 상에는 type 이 smallint 이므로 자바 내에서 그에 대응되는 Short 사용했습니다.
    // year 라는 컬럼명이 sql 함수명과 충돌을 일으켜서 credit_year 라는 컬럼명으로 변경했습니다.
    @Column(nullable = false, name = "credit_year")
    private Short year;

    // 사용자가 크레딧을 몇 월에 획득했는지 나타냅니다.
    // ERD 상에는 type 이 smallint 이므로 자바 내에서 그에 대응되는 Short 사용했습니다.
    // month 라는 컬럼명이 sql 함수명과 충돌을 일으켜서 credit_month 라는 컬럼명으로 변경했습니다.
    // @Range 어노테이션 사용하여 1 ~ 12 까지의 값만을 가질 수 있도록 설정했습니다.
    @Column(nullable = false, name = "credit_month")
    @Range(min = 1, max = 12)
    private Short month;

    // 사용자가 획득한 크레딧의 양을 나타냅니다.
    @Column(nullable = false)
    private Long amount;
}
