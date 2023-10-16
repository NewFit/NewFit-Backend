package com.newfit.reservation.domains.dev.service;

import com.newfit.reservation.domains.dev.domain.Proposal;
import com.newfit.reservation.domains.dev.dto.request.CreateProposalRequest;
import com.newfit.reservation.domains.dev.repository.ProposalRepository;
import com.newfit.reservation.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProposalService {        // ProposalRepository 에 단순 위임하는 Service 클래스입니다.

    private final ProposalRepository proposalRepository;

    public void saveProposal(User user, CreateProposalRequest request) {
        Proposal proposal = Proposal.createProposal(user, request.getName(), request.getContent());
        proposalRepository.save(proposal);
    }
}
