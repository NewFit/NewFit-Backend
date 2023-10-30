package com.newfit.reservation.domains.dev.repository;

import com.newfit.reservation.domains.dev.domain.Proposal;
import com.newfit.reservation.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findAllByUser(User user);
}
