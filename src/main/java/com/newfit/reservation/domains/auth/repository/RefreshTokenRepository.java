package com.newfit.reservation.domains.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.newfit.reservation.domains.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);
}
