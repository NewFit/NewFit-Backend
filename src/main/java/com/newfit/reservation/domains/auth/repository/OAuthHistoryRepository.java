package com.newfit.reservation.domains.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.auth.domain.OAuthHistory;
import com.newfit.reservation.domains.auth.domain.ProviderType;

public interface OAuthHistoryRepository extends JpaRepository<OAuthHistory, Long> {
	Optional<OAuthHistory> findByProviderTypeAndAttributeName(ProviderType providerType, String attributeName);
}
