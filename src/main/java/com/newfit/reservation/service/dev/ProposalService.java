package com.newfit.reservation.service.dev;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.dev.Proposal;
import com.newfit.reservation.dto.request.CreateProposalRequest;

import com.newfit.reservation.repository.dev.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProposalService {        // ProposalRepository 에 단순 위임하는 Service 클래스입니다.

    private final ProposalRepository proposalRepository;

    public Long saveProposal(User user, CreateProposalRequest requestDto) {

        Proposal proposal = Proposal.createProposal(user, requestDto.getName(), requestDto.getContent());
        return proposalRepository.save(proposal);
    }

    public Optional<Proposal> findOneById(Long proposalId) {
        return proposalRepository.findOne(proposalId);
    }

    public List<Proposal> findAllProposals() {
        return proposalRepository.findAll();
    }
}
