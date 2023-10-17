package com.newfit.reservation.domains.user.repository;

import com.newfit.reservation.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
}
