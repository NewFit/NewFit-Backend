package com.newfit.reservation.domains.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.dev.domain.Proposal;
import com.newfit.reservation.domains.user.domain.User;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

	List<Proposal> findAllByUser(User user);
}
