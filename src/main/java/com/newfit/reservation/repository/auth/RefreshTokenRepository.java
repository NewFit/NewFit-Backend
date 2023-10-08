package com.newfit.reservation.repository.auth;

import com.newfit.reservation.domain.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}
