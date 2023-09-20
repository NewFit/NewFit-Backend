package com.newfit.reservation.repository;

import com.newfit.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
