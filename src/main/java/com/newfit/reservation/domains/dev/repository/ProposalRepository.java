package com.newfit.reservation.domains.dev.repository;

import com.newfit.reservation.domains.dev.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
