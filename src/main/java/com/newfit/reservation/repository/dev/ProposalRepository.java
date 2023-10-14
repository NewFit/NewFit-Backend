package com.newfit.reservation.repository.dev;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.dev.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllByUser(User user);
}
