package com.newfit.reservation.repository;

import com.newfit.reservation.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    // User 엔티티 객체를 DB 에 저장하는 메소드입니다.
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    // 사용자의 회원 정보를 업데이트(변경)하는 메소드입니다.
    // 사용자 프로필 사진 정보는 나중에 포함하겠습니다.
    public Long update(User updateUser) {

        // 업데이트할 user 조회합니다.
        User findUser = em.find(User.class, updateUser.getId());

        // 사용자가 이메일 수정사항을 제출했으면 이메일 업데이트합니다.
        if(updateUser.getEmail() != null) {
            findUser.updateEmail(updateUser.getEmail());
        }

        // 사용자가 닉네임(별명) 수정사항을 제출했으면 닉네임 업데이트합니다.
        if(updateUser.getNickname() != null) {
            findUser.updateNickname(updateUser.getNickname());
        }

        //사용자가 핸드폰 번호 수정사항을 제출했으면 핸드폰 번호 업데이트합니다.
        if(updateUser.getTel() != null) {
            findUser.updateTel(updateUser.getTel());
        }

        return updateUser.getId();
    }

    // DB 에서 User 엔티티 객체의 id 를 통해 조회하는 메소드입니다.
    public Optional<User> findOne(Long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    //DB 에서 User 엔티티 객체를 삭제하는 메소드입니다.
    public Long delete(Long userId) {
        User findUser = em.find(User.class, userId);
        em.remove(findUser);

        return userId;
    }
}
