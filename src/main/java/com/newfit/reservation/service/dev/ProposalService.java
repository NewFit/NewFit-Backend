package com.newfit.reservation.service.dev;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.dev.Proposal;
import com.newfit.reservation.dto.request.CreateProposalRequest;
import com.newfit.reservation.repository.dev.ProposalRepository;
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
