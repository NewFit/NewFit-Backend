package com.newfit.reservation.repository.auth;

import com.newfit.reservation.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
