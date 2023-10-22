package com.newfit.reservation.domains.auth.repository;

import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.auth.domain.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthHistoryRepository extends JpaRepository<OAuthHistory, Long> {
    Optional<OAuthHistory> findByProviderTypeAndAttributeName(ProviderType providerType, String attributeName);
}
