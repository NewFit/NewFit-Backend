package com.newfit.reservation.service;

import com.newfit.reservation.domain.Proposal;
import com.newfit.reservation.domain.Report;
import com.newfit.reservation.dto.request.CreateProposalRequestDto;

import com.newfit.reservation.repository.ProposalRepository;
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

    public Long saveProposal(CreateProposalRequestDto proposalRequestDto) {

        // CreateProposalRequestDto 객체를 Proposal 엔티티 객체로 변환하는 메소드 사용했습니다.
        Proposal proposal = CreateProposalRequestDto.proposalDto2Entity(proposalRequestDto);
        return proposalRepository.save(proposal);
    }

    public Optional<Proposal> findOneById(Long proposalId) {
        return proposalRepository.findOne(proposalId);
    }

    public List<Proposal> findAllProposals() {
        return proposalRepository.findAll();
    }
}
