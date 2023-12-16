package com.newfit.reservation.domains.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);
}
