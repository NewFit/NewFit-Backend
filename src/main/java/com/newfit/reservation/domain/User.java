package com.newfit.reservation.domain;

import com.newfit.reservation.domain.common.BaseTimeEntity;
import com.newfit.reservation.domain.dev.Proposal;
import com.newfit.reservation.domain.dev.Report;
import com.newfit.reservation.dto.request.UserSignUpRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  /* Entity 클래스는 기본생성자가 필수로 있어야 합니다. 다만, Entity 객체를
                                                      직접 생성자로 생성할 일은 없을듯 하여 protected 로 설정했습니다.*/
@Table(name = "users")  // default 이름인 user 는 sql 예약어와 겹쳐서 users 로 테이블 이름 변경했습니다.
public class User extends BaseTimeEntity {

    // User 테이블 PK 입니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실제 사용자의 이름을 나타냅니다.
    @Column(nullable = false)
    private String username;

    // 사용자의 이메일을 나타냅니다.
    // email validation 을 위해 어노테이션 추가했습니다.
    @Email
    @Column(nullable = false)
    private String email;

    // 가장 최근 로그인한 시점을 나타냅니다.
    @Column(name = "last_login_at", nullable = false)
    private LocalDateTime lastLoginAt;

    // 사용자의 핸드폰 번호를 나타냅니다.
    @Column(nullable = false)
    private String tel;

    // 사용자의 닉네임(별명)을 나타냅니다. unique 제약이 붙습니다.
    @Column(nullable = false, unique = true)
    private String nickname;

    // 사용자가 Oauth 2.0 로그인할 때 이용한 OAuth 제공자를 나타냅니다.
    // Provider 라는 Enum 타입을 새로 정의했습니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    // 가장 최근 출입한 시점을 나타냅니다.
    @Column(name = "tag_at")
    private LocalDateTime tagAt;

    // 사용자의 크레딧 잔여량을 나타냅니다.
    @Column(nullable = false)
    private Long balance;

    // 사용자의 프로필 사진 정보를 나타냅니다.
    @Column(name = "file_path", nullable = false)
    private String filePath;

    // 양방향 연관관계를 나타냅니다.
    @OneToMany(mappedBy = "user")
    private List<Report> reportList = new ArrayList<>();

    // 양방향 연관관계를 나타냅니다.
    @OneToMany(mappedBy = "user")
    private List<Proposal> proposalList = new ArrayList<>();

    // 양방향 연관관계를 나타냅니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Authority> authorityList = new ArrayList<>();


    /* =========== update method  =========== */

    public void updateEmail(String email) {

        this.email = email;

    }

    public void updateTel(String tel) {

        this.tel = tel;

    }

    public void updateNickname(String nickname) {

        this.nickname = nickname;

    }

    public void updateFilePath(String filePath) {

        this.filePath = filePath;

    }

    public void addBalance(Long balance) {

        this.balance += balance;

    }

    public Long getTermCredit(LocalDateTime term) {
        return this.authorityList
                .stream()
                .map(authority -> authority.getTermCredit(term))
                .mapToLong(Long::longValue)
                .sum();
    }

    @Builder
    private User(UserSignUpRequest userInfo, Provider provider) {
        this.username = userInfo.getUsername();
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.tel = userInfo.getTel();
        this.provider = provider;
        this.balance = 0L;
        this.lastLoginAt = LocalDateTime.now();
        this.filePath = "https://newfit-image.s3.ap-northeast-2.amazonaws.com/newfitIcon.png";
    }

    public static User userSignUp(UserSignUpRequest request, Provider provider) {
        return User.builder()
                .userInfo(request)
                .provider(provider)
                .build();
    }
}
