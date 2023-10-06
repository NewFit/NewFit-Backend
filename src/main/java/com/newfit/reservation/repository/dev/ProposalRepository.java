package com.newfit.reservation.repository.dev;

import com.newfit.reservation.domain.dev.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
