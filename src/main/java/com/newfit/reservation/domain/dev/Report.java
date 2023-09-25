package com.newfit.reservation.domain.dev;

import com.newfit.reservation.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Entity 클래스는 기본생성자가 필수로 있어야 합니다.
                                                    // 다만, Entity 객체를 직접 생성자로 생성할 일은 없을듯 하여 protected 로 설정했습니다.
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Report {

    // Report 테이블 PK 입니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 테이블과의 연관관계를 나타내는 FK 입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 버그 제목을 나타냅니다.
    @Column(nullable = false)
    private String subject;

    // 버그 내용을 나타냅니다.
    @Column(nullable = false)
    private String content;

    @Builder
    private Report(User user, String subject, String content) {
        this.user = user;
        this.subject = subject;
        this.content = content;
    }

    public static Report createReport(User user, String subject, String content) {
        return Report.builder()
                .user(user)
                .subject(subject)
                .content(content)
                .build();
    }

    // 연관관계 편의 메소드입니다.
    public void setUserRelation(User user) {
        this.user = user;
        user.getReportList().add(this);
    }
}

