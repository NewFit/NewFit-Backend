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
public class Proposal {

    // Proposal 테이블 PK 입니다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 테이블과의 연관관계를 나타내는 FK 입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 기능 이름을 나타냅니다.
    @Column(nullable = false)
    private String name;

    // 기능 내용을 나타냅니다.
    @Column(nullable = false)
    private String content;

    @Builder
    private Proposal(User user, String name, String content) {
        this.user = user;
        this.name = name;
        this.content = content;
    }

    public static Proposal createProposal(User user, String name, String content) {
        return Proposal.builder()
                .user(user)
                .name(name)
                .content(content)
                .build();
    }

    // 연관관계 편의 메소드입니다.
    public void setUserRelation(User user) {
        this.user = user;
        user.getProposalList().add(this);
    }
}

