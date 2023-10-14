package com.newfit.reservation.domain;

import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Entity 클래스는 기본생성자가 필수로 있어야 합니다.
                                                    // 다만, Entity 객체를 직접 생성자로 생성할 일은 없을듯 하여 protected 로 설정했습니다.
public class Authority extends BaseTimeEntity {

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

    @Range(min = 0, max = 10)
    @Column(nullable = false, name = "credit_acquisition_count")
    private Short creditAcquisitionCount;

    @Column(name = "tag_at")
    private LocalDateTime tagAt;

    // 양방향 연관관계를 나타냅니다.
    @OneToMany(mappedBy = "authority", cascade = CascadeType.REMOVE)
    private List<Credit> creditList = new ArrayList<>();

    @OneToMany(mappedBy = "authority", cascade = CascadeType.REMOVE)
    private List<Reservation> reservationList = new ArrayList<>();

    // accepted 필드값을 true로 업데이트하는 메소드입니다.
    public void acceptUser() {
        this.accepted = true;
    }

    public void updateTagAt(LocalDateTime tagAt) {
        this.tagAt = tagAt;
    }

    public void incrementAcquisitionCount() {
        this.creditAcquisitionCount++;
    }

    public void resetAcquisitionCount() {
        this.creditAcquisitionCount = 0;
    }

    //========= 연관관계 편의 메소드입니다. =========//
    public void setUserRelation(User user) {
        this.user = user;
        user.getAuthorityList().add(this);
    }

    // Credit 클래스에 있던 연관관계 편의 메소드를 없애고 Authority 클래스에 추가했습니다.
    public void setCreditRelation(Credit credit) {
        this.creditList.add(credit);
        credit.updateAuthority(this);
    }

    // Gym과의 연관관계 편의 메소드는 논의가 필요합니다.
    @Builder(access = AccessLevel.PRIVATE)
    private Authority(User user, Gym gym) {
        this.user = user;
        this.gym = gym;
        this.accepted = false;
        this.role = Role.USER;
        this.creditAcquisitionCount = 0;
    }

    public static Authority createAuthority(User user, Gym gym) {
        return Authority.builder()
                .user(user)
                .gym(gym)
                .build();
    }

    public Long getTermCredit(LocalDateTime term) {
        return this.creditList
                .stream()
                .filter(credit -> credit.getYear().equals((short) term.getYear())
                        && credit.getMonth().equals((short) term.getMonthValue())
                )
                .findAny()
                .map(Credit::getAmount)
                .orElse(0L);
    }
}
