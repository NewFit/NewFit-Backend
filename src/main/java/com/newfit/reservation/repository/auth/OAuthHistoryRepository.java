package com.newfit.reservation.repository.auth;

import com.newfit.reservation.domain.auth.OAuthHistory;
import com.newfit.reservation.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthHistoryRepository extends JpaRepository<OAuthHistory, Long> {
    Optional<OAuthHistory> findByProviderAndAttributeName(Provider provider, String attributeName);
}
